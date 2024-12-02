package com.movie.recommender.crawler.service;

import com.mongodb.MongoException;
import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.common.model.movie.NowPlayingMoviesByCountry;
import com.movie.recommender.common.model.showtime.ShowtimesByCity;
import com.movie.recommender.crawler.repository.MovieRepository;
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
    private final String NOW_PLAYING_COLLECTION = "NowPlayingByCountry";
    private final String SHOWTIMES_COLLECTION = "ShowtimesByCity";

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

    private void removeOldNowPlayingMovies(String countryCode) {
        Query query = new Query(Criteria.where("countryCode").is(countryCode));
        mongoTemplate.remove(query, NOW_PLAYING_COLLECTION);
        log.info("Old now playing movies for country '{}' removed from '{}'.", countryCode, NOW_PLAYING_COLLECTION);
    }

    public void insertNowPlayingMovies(NowPlayingMoviesByCountry nowPlayingMovies) {
        try {
            String countryCode = nowPlayingMovies.getCountryCode();

            removeOldNowPlayingMovies(countryCode);

            mongoTemplate.insert(nowPlayingMovies, NOW_PLAYING_COLLECTION);
            log.info("Now playing movies for country '{}' inserted into '{}' collection.", countryCode, NOW_PLAYING_COLLECTION);
        } catch (MongoException e) {
            log.error("Error inserting now playing movies for country '{}': {}", nowPlayingMovies.getCountryCode(), e.getMessage(), e);
        }
    }

    public boolean existsMovieById(Long movieId) {
        Query query = new Query(Criteria.where("id").is(movieId));
        return mongoTemplate.exists(query, MovieDetails.class);
    }

    public List<NowPlayingMoviesByCountry> getNowPlayingMovies() {
        try {
            return mongoTemplate.findAll(NowPlayingMoviesByCountry.class, NOW_PLAYING_COLLECTION);
        } catch (MongoException e) {
            log.error("Error fetching now playing movies: {}", e.getMessage(), e);
            return List.of();
        }
    }

    private void removeOldShowtimes(String countryCode, String cityName) {
        Query query = new Query(
                Criteria.where("countryCode").is(countryCode)
                        .and("cityName").is(cityName)
        );
        mongoTemplate.remove(query, SHOWTIMES_COLLECTION);
        log.info("Old showtimes for city '{}' in country '{}' removed from 'ShowtimesByCity' collection.", cityName, countryCode);
    }

    public void insertShowtimes(ShowtimesByCity showtimesByCity) {
        try {
            String countryCode = showtimesByCity.getCountryCode();
            String cityName = showtimesByCity.getCityName();

            // Remove old showtimes for the given city and country
            removeOldShowtimes(countryCode, cityName);

            // Insert new showtimes
            mongoTemplate.insert(showtimesByCity, SHOWTIMES_COLLECTION);
            log.info("Showtimes for city '{}' in country '{}' inserted into 'ShowtimesByCity' collection.", cityName, countryCode);
        } catch (MongoException e) {
            log.error("Error inserting showtimes for city '{}' in country '{}': {}", showtimesByCity.getCityName(), showtimesByCity.getCountryCode(), e.getMessage(), e);
        }
    }

    public ShowtimesByCity getShowtimesByCity(String countryName, String cityName) {
        try {
            Query query = new Query(
                    Criteria.where("countryName").is(countryName)
                            .and("cityName").is(cityName)
            );
            return mongoTemplate.findOne(query, ShowtimesByCity.class, SHOWTIMES_COLLECTION);
        } catch (MongoException e) {
            log.error("Error fetching showtimes for city '{}' in country '{}': {}", cityName, countryName, e.getMessage(), e);
            return null;
        }
    }



}