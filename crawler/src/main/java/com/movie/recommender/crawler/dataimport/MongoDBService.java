package com.movie.recommender.crawler.dataimport;

import com.movie.recommender.common.model.movie.MovieDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MongoDBService {
    public void insertMovies(List<MovieDetails> movieDetailsList) {
        log.info("Inserting movie data into MongoDB...");
        // Actual MongoDB insertion logic goes here
        // e.g., mongoDatabase.getCollection("movies").insertMany(movieDetailsList);
        log.info("Movie data insertion into MongoDB completed.");
    }
}