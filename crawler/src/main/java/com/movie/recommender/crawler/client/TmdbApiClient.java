package com.movie.recommender.crawler.client;

import com.movie.recommender.common.model.Movie;
import com.movie.recommender.lib.http.HttpClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TmdbApiClient {
    public static final String API_KEY = System.getenv("TMDB_API_KEY");

    private final HttpClient httpClient;

    public TmdbApiClient() {
        this.httpClient = new HttpClient(API_KEY);
    }

    public Movie getMovieDetails(String movieId) {
        try {
            log.info("Fetching movie details for movie ID: {}", movieId);
            return httpClient.get(UrlBuilder.movieDetailsUrl(movieId), Movie.class);
        } catch (Exception e) {
            log.error("Failed to fetch movie details for movie ID: {}", movieId, e);
            return null;
        }
    }
}
