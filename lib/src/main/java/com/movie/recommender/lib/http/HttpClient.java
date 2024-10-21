package com.movie.recommender.lib.http;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class HttpClient {
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final String authToken;

    public HttpClient(String authToken) {
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.authToken = authToken;
    }

    public <T> T get(String url, Class<T> tClass) throws IOException {
        Request.Builder requestBuilder = new Request.Builder().url(url);

        // Add Authorization header if auth token is present
        if (authToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = requestBuilder.build();

        // Make the HTTP GET request
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }

            assert response.body() != null;

            // Convert the response body into the specified class type
            return objectMapper.readValue(response.body().string(), tClass);
        }
    }
}
