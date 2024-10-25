package com.movie.recommender.crawler.client;

import com.movie.recommender.common.model.MovieShowtimesResponse;
import com.movie.recommender.lib.http.AuthProvider;
import com.movie.recommender.lib.http.HttpClient;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

@Slf4j
public class SerpApiClient {

    private final HttpClient httpClient;

    public SerpApiClient() {
        this.httpClient = new HttpClient(new AuthProvider(null, getApiKey()));
    }

    public Optional<MovieShowtimesResponse > getMovieShowtimes(String movieName, String location, String language) {
        String url = UrlBuilder.movieShowtimesUrl(movieName, location, language);
        return callApi(url, MovieShowtimesResponse.class);
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


    private static String getApiKey() {
        String apiKey = System.getenv("SERP_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("SERP_API_KEY environment variable is not set.");
        }
        return apiKey;
    }
}