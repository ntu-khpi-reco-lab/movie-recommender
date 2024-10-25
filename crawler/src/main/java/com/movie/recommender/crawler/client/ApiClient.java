package com.movie.recommender.crawler.client;

import com.movie.recommender.lib.http.AuthProvider;
import com.movie.recommender.lib.http.HttpClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public abstract class ApiClient {
    protected final HttpClient httpClient;
    protected final String apiKey; // Make apiKey accessible to subclasses

    protected ApiClient(String apiKeyEnvVar) {
        this.apiKey = getApiKey(apiKeyEnvVar);
        this.httpClient = new HttpClient(new AuthProvider(this.apiKey, null));
    }

    private static String getApiKey(String apiKeyEnvVar) {
        String apiKey = System.getenv(apiKeyEnvVar);
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException(apiKeyEnvVar + " environment variable is not set.");
        }
        return apiKey;
    }

    protected <T> Optional<T> callApi(String url, Class<T> responseType) {
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
}