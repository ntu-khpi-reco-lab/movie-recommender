package com.movie.recommender.crawler.repository;

import com.movie.recommender.common.model.movie.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NowPlayingMovieRepository extends MongoRepository<Movie, String> {
}