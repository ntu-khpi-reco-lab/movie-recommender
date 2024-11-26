package com.movie.recommender.reco.service;

import com.mongodb.MongoException;
import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.common.model.movie.NowPlayingMoviesByCountry;
import com.movie.recommender.common.model.showtime.Showtime;
import com.movie.recommender.common.model.showtime.ShowtimesByCity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MongoDBService {
    private final MongoTemplate mongoTemplate;
    private final String NOW_PLAYING_COLLECTION = "NowPlayingByCountry";

    public MongoDBService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<NowPlayingMoviesByCountry> getNowPlayingMoviesByCountry(String countryCode) {
        try {
            Query query = new Query(Criteria.where("countryCode").is(countryCode));
            return mongoTemplate.find(query, NowPlayingMoviesByCountry.class, NOW_PLAYING_COLLECTION);
        } catch (MongoException e) {
            log.error("Error fetching now playing movies for country '{}': {}", countryCode, e.getMessage(), e);
            return List.of();
        }
    }

    public MovieDetails getMovieDetailsById(Long movieId) {
        Query query = new Query(Criteria.where("id").is(movieId));
        return mongoTemplate.findOne(query, MovieDetails.class);
    }

    public List<Showtime> getMovieShowtimesByCity(String countryCode, String cityName, Long movieId) {
        log.info("Fetching showtimes for countryCode: {}, cityName: {}, movieId: {}", countryCode, cityName, movieId);

        Query query = new Query(Criteria.where("countryCode").is(countryCode)
                .and("cityName").is(cityName));

        ShowtimesByCity showtimesByCity = mongoTemplate.findOne(query, ShowtimesByCity.class, "ShowtimesByCity");

        if (showtimesByCity != null) {
            log.info("Found ShowtimesByCity for countryCode: {}, cityName: {}", countryCode, cityName);

            for (ShowtimesByCity.MovieShowtimes movie : showtimesByCity.getMovies()) {
                if (movie.getId().equals(movieId)) {
                    log.info("Showtimes found for movieId: {}", movieId);
                    return movie.getShowtimes();
                }
            }
        }

        log.warn("No showtimes found for countryCode: {}, cityName: {}, movieId: {}", countryCode, cityName, movieId);
        return Collections.emptyList();
    }

}