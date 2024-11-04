package com.movie.recommender.crawler.service;
import com.mongodb.MongoException;
import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.crawler.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MongoDBService {

    private final MovieRepository movieRepository;

    public MongoDBService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
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
}