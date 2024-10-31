package com.movie.recommender.crawler;


import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.crawler.dataimport.MongoDBService;
import com.movie.recommender.crawler.dataimport.MovieDataInitializer;
import com.movie.recommender.crawler.dataimport.MovieDataLoader;


import java.util.List;

public class Main {

    public static void initializeMovieData() {
        String datasetPath = System.getenv("DATASETPATH");

        if (datasetPath == null) {
            System.err.println("DATASETPATH environment variable is not set.");
            return;
        }

        // Initialize data loader, MongoDB service, and data initializer
        MovieDataLoader movieDataLoader = new MovieDataLoader();
        MongoDBService mongoDBService = new MongoDBService();
        MovieDataInitializer dataInitializer = new MovieDataInitializer(movieDataLoader, mongoDBService);

        // Load data and handle MongoDB insertion
        dataInitializer.initializeData(datasetPath);
    }

    public static void main(String[] args) {
        System.out.println("Crawler started");
        initializeMovieData();
    }


}
