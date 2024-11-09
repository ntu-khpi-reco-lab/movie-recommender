package com.movie.recommender.crawler.client;

import com.movie.recommender.lib.http.auth.AuthProvider;
import com.movie.recommender.lib.http.auth.BearerAuthProvider;
import com.movie.recommender.common.model.movie.Movie;
import com.movie.recommender.common.model.movie.MovieList;
import com.movie.recommender.common.model.movie.MovieCredits;
import com.movie.recommender.common.model.movie.MovieKeywords;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TmdbApiClient extends ApiClient {
    public TmdbApiClient() {
        super();
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

    @Override
    protected AuthProvider createAuthProvider() {
        String authToken = getApiKey("TMDB_API_KEY");
        return new BearerAuthProvider(authToken);
    }
}
