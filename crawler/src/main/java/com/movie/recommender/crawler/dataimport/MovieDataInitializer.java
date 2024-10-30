package com.movie.recommender.crawler.dataimport;


import com.movie.recommender.common.model.movie.MovieDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MovieDataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(MovieDataInitializer.class);
    private final MovieDataLoader movieDataLoader;

    public MovieDataInitializer(MovieDataLoader movieDataLoader) {
        this.movieDataLoader = movieDataLoader;
    }

    public void initializeData(String filePath) {
        // Load data using MovieDataLoader
        List<MovieDetails> movieDetailsList = movieDataLoader.parseMovieData(filePath);
        logger.info("Loaded {} movie records from {}", movieDetailsList.size(), filePath);

        // Placeholder for MongoDB insertion logic
        insertDataIntoMongoDB(movieDetailsList);
    }

    private void insertDataIntoMongoDB(List<MovieDetails> movieDetailsList) {
        // Placeholder logic for MongoDB insertion
        // For example: mongoDatabase.getCollection("movies").insertMany(movieDetailsList);
        logger.info("Inserting movie data into MongoDB...");
        // Future MongoDB insertion logic goes here
    }

    public static void main(String[] args) {
        String datasetPath = System.getenv("DATASETPATH");

        // Initialize data loader and data initializer
        MovieDataLoader movieDataLoader = new MovieDataLoader();
        MovieDataInitializer dataInitializer = new MovieDataInitializer(movieDataLoader);

        // Load data and handle MongoDB insertion
        dataInitializer.initializeData(datasetPath);
    }
}