package com.movie.recommender.crawler.dataimport;

import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.crawler.service.MongoDBService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Component
@Slf4j
public class MovieDataInitializer {
    private final MovieDataLoader movieDataLoader;
    private final MongoDBService mongoDBService;

    @Value("${dataset.path}")
    private String datasetPath;

    public MovieDataInitializer(MovieDataLoader movieDataLoader, MongoDBService mongoDBService) {
        this.movieDataLoader = movieDataLoader;
        this.mongoDBService = mongoDBService;
    }

    public void initializeData() {
        if (mongoDBService.isDataInitialized()) {
            log.info("Movie data already initialized. Skipping data loading.");
            return;
        }

        List<MovieDetails> movieDetailsList = movieDataLoader.parseMovieData(datasetPath);
        log.info("Loaded {} movie records from {}", movieDetailsList.size(), datasetPath);
        mongoDBService.insertMovies(movieDetailsList);
    }
}