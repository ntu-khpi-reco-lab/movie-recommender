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
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MongoDBService {
    private final MongoTemplate mongoTemplate;
    private final String NOW_PLAYING_COLLECTION = "NowPlayingByCountry";

    public MongoDBService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<NowPlayingMoviesByCountry> getNowPlayingMoviesByCountry(String countryName) {
        try {
            Query query = new Query(Criteria.where("countryName").is(countryName));
            return mongoTemplate.find(query, NowPlayingMoviesByCountry.class, NOW_PLAYING_COLLECTION);
        } catch (MongoException e) {
            log.error("Error fetching now playing movies for country '{}': {}", countryName, e.getMessage(), e);
            return List.of();
        }
    }

    public MovieDetails getMovieDetailsById(Long movieId) {
        Query query = new Query(Criteria.where("id").is(movieId));
        return mongoTemplate.findOne(query, MovieDetails.class);
    }

    public Map<Long, List<Showtime>> getMovieShowtimesByCity(String countryName, String cityName, List<Long> movieIds) {
        log.info("Fetching showtimes for countryCode: {}, cityName: {}, movieIds: {}", countryName, cityName, movieIds);

        // Build the query with countryCode and cityName
        Query query = new Query(Criteria.where("countryName").is(countryName)
                .and("cityName").is(cityName));

        // Fetch the matching ShowtimesByCity record
        ShowtimesByCity showtimesByCity = mongoTemplate.findOne(query, ShowtimesByCity.class, "ShowtimesByCity");

        if (showtimesByCity != null && showtimesByCity.getMovies() != null) {
            log.info("ShowtimesByCity record found for countryCode: {}, cityName: {}", countryName, cityName);

            // Create a map of movieId to showtimes, adding null checks
            return showtimesByCity.getMovies().stream()
                    .filter(movie -> movie != null && movie.getId() != null && movieIds.contains(movie.getId())) // Check for null movie and id
                    .collect(Collectors.toMap(
                            movie -> movie.getId(), // Get movie ID
                            movie -> movie.getShowtimes() != null ? movie.getShowtimes() : Collections.emptyList() // Handle null showtimes
                    ));
        } else {
            log.warn("No ShowtimesByCity record found for countryCode: {}, cityName: {}", cityName, cityName);
            return Collections.emptyMap();
        }
    }
}