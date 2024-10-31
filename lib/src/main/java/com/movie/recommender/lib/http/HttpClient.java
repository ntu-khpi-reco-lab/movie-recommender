package com.movie.recommender.lib.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.movie.recommender.lib.http.auth.AuthProvider;
import com.movie.recommender.lib.http.auth.ZeroAuthProvider;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;

public class HttpClient {
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final AuthProvider authProvider;

    public HttpClient(){
        this.client = new OkHttpClient();
        this.objectMapper = createObjectMapper();
        this.authProvider = new ZeroAuthProvider();
    }

    public HttpClient(AuthProvider authProvider) {
        this.client = new OkHttpClient();
        this.objectMapper = createObjectMapper();
        this.authProvider = authProvider;
    }

    public <T> T get(String url, Class<T> tClass) throws IOException {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        Request.Builder requestBuilder = new Request.Builder();

        // Apply authentication based on the provider type
        authProvider.applyAuth(requestBuilder, urlBuilder);

        Request request = requestBuilder.url(urlBuilder.build()).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }
            if (response.body() == null) {
                throw new IOException("Response body is null");
            }
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
