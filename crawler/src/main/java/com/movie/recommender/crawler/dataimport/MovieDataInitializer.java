package com.movie.recommender.crawler.dataimport;

import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.crawler.repository.MovieRepository;
import com.movie.recommender.crawler.service.MongoDBService;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Component
@Slf4j
public class MovieDataInitializer {
    private final MovieDataLoader movieDataLoader;

    public MovieDataInitializer(MovieDataLoader movieDataLoader, MovieRepository movieRepository) {
        this.movieDataLoader = movieDataLoader;
    }

    public List<MovieDetails>  initializeData(String filePath) {
        List<MovieDetails> movieDetailsList = movieDataLoader.parseMovieData(filePath);
        log.info("Loaded {} movie records from {}", movieDetailsList.size(), filePath);
        return movieDetailsList;
    }
}