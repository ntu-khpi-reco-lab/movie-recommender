package com.movie.recommender.crawler.dataimport;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.movie.recommender.common.model.movie.MovieDetails;

public interface MovieRepository extends MongoRepository<MovieDetails, String> {
}