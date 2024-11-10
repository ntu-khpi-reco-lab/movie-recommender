package com.movie.recommender.crawler.service;

import com.mongodb.MongoException;
import com.movie.recommender.common.model.movie.Movie;
import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.common.model.movie.NowPlayingMoviesByCountry;
import com.movie.recommender.crawler.repository.MovieRepository;
import com.movie.recommender.crawler.repository.NowPlayingMovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MongoDBService {
    private final MovieRepository movieRepository;
    private final MongoTemplate mongoTemplate;

    public MongoDBService(MovieRepository movieRepository, MongoTemplate mongoTemplate) {
        this.movieRepository = movieRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public boolean isDataInitialized() {
        long count = movieRepository.count();
        log.info("Movie data record count in MongoDB: {}", count);
        return count > 0;
    }

    public void insertMovies(List<MovieDetails> movieDetailsList) {
        try {
            log.info("Inserting movie data into MongoDB...");
            movieRepository.saveAll(movieDetailsList);
            log.info("Movie data insertion into MongoDB completed.");
        } catch (MongoException e) {
            log.error("Error inserting movie data into MongoDB: {}", e.getMessage(), e);
        }
    }

    public void clearNowPlayingCollection() {
        try {
            mongoTemplate.dropCollection("NowPlaying");
            log.info("Collection 'NowPlaying' cleared successfully.");
        } catch (MongoException e) {
            log.error("Error clearing 'NowPlaying' collection: {}", e.getMessage(), e);
        }
    }

    public void insertNowPlayingMovies(NowPlayingMoviesByCountry nowPlayingMovies) {
        try {
            mongoTemplate.dropCollection("NowPlayingByCountry");
            mongoTemplate.insert(nowPlayingMovies, "NowPlayingByCountry");
            log.info("Now playing movies inserted into 'NowPlayingByCountry' collection.");
        } catch (MongoException e) {
            log.error("Error inserting now playing movies: {}", e.getMessage(), e);
        }
    }

    public List<NowPlayingMoviesByCountry> getNowPlayingMovies(String country) {
        try {
            return mongoTemplate.find(
                    Query.query(Criteria.where("country").is(country)),
                    NowPlayingMoviesByCountry.class,
                    "NowPlayingByCountry"
            );
        } catch (MongoException e) {
            log.error("Error fetching now playing movies by country: {}", e.getMessage(), e);
            return List.of();
        }
    }
}