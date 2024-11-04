package com.movie.recommender.crawler.dataimport;

import com.movie.recommender.common.model.movie.MovieDetails;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Component
@Slf4j
public class MovieDataInitializer {
    private final MovieDataLoader movieDataLoader;
    private final MovieRepository movieRepository;

    public MovieDataInitializer(MovieDataLoader movieDataLoader, MovieRepository movieRepository) {
        this.movieDataLoader = movieDataLoader;
        this.movieRepository = movieRepository;
    }

    public void initializeData(String filePath) {
        List<MovieDetails> movieDetailsList = movieDataLoader.parseMovieData(filePath);
        log.info("Loaded {} movie records from {}", movieDetailsList.size(), filePath);

        // Зберігаємо всі дані в MongoDB
        movieRepository.saveAll(movieDetailsList);
        log.info("Movie data insertion into MongoDB completed.");
    }
}