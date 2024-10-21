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
        try {
            log.info("Fetching movie details for movie ID: {}", movieId);

            String url = UrlBuilder.movieDetailsUrl(movieId);
            Movie movie = httpClient.get(url, Movie.class);

            return Optional.ofNullable(movie);
        } catch (Exception e) {
            log.error("Failed to fetch movie details for movie ID: {}", movieId, e);
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
