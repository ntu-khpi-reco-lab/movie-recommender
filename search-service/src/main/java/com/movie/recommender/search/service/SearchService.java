package com.movie.recommender.search.service;

import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.search.repository.MoviesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SearchService {
    final private MoviesRepository moviesRepository;

    public SearchService(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    public List<MovieDetails> getMovieByTitle(String title) {
        log.info("Searching for movies with title {}", title);
        return moviesRepository.findMoviesByTitle(title);
    }

    public List<MovieDetails> getMovieByGenres(List<String> genres) {
        log.info("Searching for movies with genres {}", genres);
        return moviesRepository.findMoviesByGenres(genres);
    }

    public List<MovieDetails> getMovieByReleaseDate(String startDate, String endDate) {
        log.info("Searching for movies with release date from {} to {}", startDate, endDate);
        return moviesRepository.findMoviesWhereReleaseDateBetween(startDate, endDate);
    }

    public List<MovieDetails> getMoviesByRating(Double ratingStart, Double ratingEnd) {
        log.info("Searching for movies with rating from {} to {}", ratingStart, ratingEnd );
        return moviesRepository.findMoviesWhereRatingBetween(ratingStart, ratingEnd);
    }

    public List<MovieDetails> getAdultMovies(){
        log.info("Searching for adult movies");
        return moviesRepository.findAdultMovies();
    }

    public List<MovieDetails> getNonAdultMovies(){
        log.info("Searching for non-adult movies");
        return moviesRepository.findNonAdultMovies(PageRequest.of(0, 10));
    }

    public List<MovieDetails> getMoviesByCastMemberNames(List<String> names) {
        log.info("Searching for movies with cast member names {}", names);
        return moviesRepository.findMoviesByCastMemberNames(names);
    }

    public List<MovieDetails> getMoviesByCrewMemberNames(List<String> names) {
        log.info("Searching for movies with crew member names {}", names);
        return moviesRepository.findMoviesByCrewMemberNames(names);
    }

    public List<MovieDetails> getMoviesByKeywords(List<String> keywords) {
        log.info("Searching for movies with keywords {}", keywords);
        return moviesRepository.findMoviesByKeywordNames(keywords);
    }
}
