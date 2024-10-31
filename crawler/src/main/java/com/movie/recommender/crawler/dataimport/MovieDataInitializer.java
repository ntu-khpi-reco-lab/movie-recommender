package com.movie.recommender.crawler.dataimport;

import com.movie.recommender.common.model.movie.MovieDetails;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MovieDataInitializer {
    private final MovieDataLoader movieDataLoader;
    private final MongoDBService mongoDBService;

    public MovieDataInitializer(MovieDataLoader movieDataLoader, MongoDBService mongoDBService) {
        this.movieDataLoader = movieDataLoader;
        this.mongoDBService = mongoDBService;
    }

    public void initializeData(String filePath) {
        if (filePath == null) {
            log.error("File path is null. Cannot load movie data.");
            return;
        }

        // Load data using MovieDataLoader
        List<MovieDetails> movieDetailsList = movieDataLoader.parseMovieData(filePath);
        log.info("Loaded {} movie records from {}", movieDetailsList.size(), filePath);

        // Insert data into MongoDB
        mongoDBService.insertDataIntoMongoDB(movieDetailsList);
    }
}