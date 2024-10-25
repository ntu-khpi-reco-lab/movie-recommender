package com.movie.recommender.lib.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class HttpClient {
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final AuthProvider authProvider;

    public HttpClient(AuthProvider authProvider) {
        this.client = new OkHttpClient();
        this.objectMapper = createObjectMapper();
        this.authProvider = authProvider;
    }

    public <T> T get(String url, Class<T> tClass) throws IOException {
        HttpUrl httpUrl = createAuthenticatedUrl(url);
        Request request = buildRequest(httpUrl);

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }
            assert response.body() != null;
            return objectMapper.readValue(response.body().string(), tClass);
        }
    }

    private HttpUrl createAuthenticatedUrl(String url) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        return authProvider.addApiKey(httpUrl);
    }

    private Request buildRequest(HttpUrl httpUrl) {
        Request.Builder requestBuilder = new Request.Builder().url(httpUrl);
        authProvider.addAuthHeader(requestBuilder);
        return requestBuilder.build();
    }

    // Create and configure ObjectMapper with necessary features
    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
