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
        this.objectMapper = createObjectMapper();
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

    //I can t be surely that I can make this manipuation with url
    public <T> T getForSerpApi(String url, Class<T> tClass) throws IOException {

        if (authToken != null) url += "&api_key=" + authToken;
        Request.Builder requestBuilder = new Request.Builder().url(url);

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

    // Create and configure ObjectMapper with necessary features
    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
