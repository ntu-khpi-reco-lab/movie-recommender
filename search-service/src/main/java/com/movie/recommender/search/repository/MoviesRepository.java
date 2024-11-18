package com.movie.recommender.search.repository;

import com.movie.recommender.common.model.movie.MovieDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface MoviesRepository extends MongoRepository<MovieDetails, String> {
    @Query("{ 'title': { '$regex': ?0, '$options': 'i' } }")
    public List<MovieDetails> findMoviesByTitle(String title);

    @Query("{ 'genres.name': { '$all': ?0 } }")
    public List<MovieDetails> findMoviesByGenres(List<String> genres);

    @Query("{ 'releaseDate': { '$gte': ?0, '$lte': ?1 } }")
    public List<MovieDetails> findMoviesWhereReleaseDateBetween(String startDate, String endDate);

    @Query("{ 'rating': {'$gte': ?0, '$lte':  ?1 } }")
    public List<MovieDetails> findMoviesWhereRatingBetween(double ratingStart, double ratingEnd);

    @Query("{ 'adult': true}")
    public List<MovieDetails> findAdultMovies();

    @Query("{ 'adult': false}")
    public List<MovieDetails> findNonAdultMovies(Pageable pageable);

    @Query("{ 'cast.name': { '$all': ?0 } }")
    List<MovieDetails> findMoviesByCastMemberNames(List<String> names);

    @Query("{ 'crew.name': { '$all': ?0} }")
    List<MovieDetails> findMoviesByCrewMemberNames(List<String> names);

    @Query("{ 'keywords.name': { '$all': ?0 } }")
    List<MovieDetails> findMoviesByKeywordNames(List<String> keywords);
}

