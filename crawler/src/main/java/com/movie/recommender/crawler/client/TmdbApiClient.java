package com.movie.recommender.crawler.client;

import com.movie.recommender.common.model.Movie;
import com.movie.recommender.lib.http.HttpClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class TmdbApiClient {
    public static final String API_KEY = getApiKey();

    private final HttpClient httpClient;

    public TmdbApiClient() {
        this.httpClient = new HttpClient(API_KEY);
    }

    public Optional<Movie> getMovieDetails(String movieId) {
        String url = UrlBuilder.movieDetailsUrl(movieId);
        return callApi(url, Movie.class);
    }

    // Helper method to handle API call
    private <T> Optional<T> callApi(String url, Class<T> responseType) {
        try {
            log.info("Making API request to URL: {}", url);
            T response = httpClient.get(url, responseType);
            log.info("Successfully fetched data from URL: {}", url);
            return Optional.ofNullable(response);
        } catch (Exception e) {
            log.error("Failed to fetch data from URL: {}", url, e);
            return Optional.empty();
        }
    }

    // Helper method to retrieve the API key from environment variables
    private static String getApiKey() {
        String apiKey = System.getenv("TMDB_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("TMDB_API_KEY environment variable is not set.");
        }
        return apiKey;
    }
}
