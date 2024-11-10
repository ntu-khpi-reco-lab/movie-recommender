package com.movie.recommender.crawler.client;

import com.movie.recommender.common.model.movie.*;
import com.movie.recommender.lib.http.auth.AuthProvider;
import com.movie.recommender.lib.http.auth.BearerAuthProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Component
@Slf4j
public class TmdbApiClient extends ApiClient {

    public TmdbApiClient() {
        super();
    }

    public Optional<Movie> getMovieDetails(String movieId) {
        String url = UrlBuilder.movieDetailsUrl(movieId);
        return callApi(url, Movie.class);
    }

    public Optional<MovieList> getNowPlayingMovies(String region) {
        String url = UrlBuilder.nowPlayingMoviesUrl(region);
        return callApi(url, MovieList.class);
    }

    public Optional<NowPlayingMoviesByCountry> getNowPlayingMoviesVar2(String region) {
        String url = UrlBuilder.nowPlayingMoviesUrl(region);
        return callApi(url, NowPlayingMoviesByCountry.class);
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
