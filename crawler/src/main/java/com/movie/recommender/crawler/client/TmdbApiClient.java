package com.movie.recommender.crawler.client;

import com.movie.recommender.common.model.Movie;
import com.movie.recommender.common.model.MovieList;
import com.movie.recommender.common.model.MovieCredits;
import com.movie.recommender.common.model.MovieKeywords;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class TmdbApiClient extends ApiClient {

    public TmdbApiClient() {
        super("TMDB_API_KEY");
    }

    public Optional<Movie> getMovieDetails(String movieId) {
        String url = UrlBuilder.movieDetailsUrl(movieId);
        return callApi(url, Movie.class);
    }

    public Optional<MovieList> getNowPlayingMovies() {
        String url = UrlBuilder.nowPlayingMoviesUrl();
        return callApi(url, MovieList.class);
    }

    public Optional<MovieCredits> getMovieCredits(String movieId) {
        String url = UrlBuilder.movieCreditsUrl(movieId);
        return callApi(url, MovieCredits.class);
    }

    public Optional<MovieKeywords> getMovieKeywords(String movieId) {
        String url = UrlBuilder.movieKeywordsUrl(movieId);
        return callApi(url, MovieKeywords.class);
    }

}
