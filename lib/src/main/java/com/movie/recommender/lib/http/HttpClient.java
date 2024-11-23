package com.movie.recommender.lib.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.movie.recommender.lib.http.auth.AuthProvider;
import com.movie.recommender.lib.http.auth.NoAuthProvider;
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

    public HttpClient() {
        this(new NoAuthProvider());
    }

    public HttpClient(AuthProvider authProvider) {
        this.client = new OkHttpClient();
        this.objectMapper = createObjectMapper();
        this.authProvider = authProvider;
    }

    public <T> T get(String url, Class<T> tClass) {
        Request request = buildRequest(url, null, "GET", null);
        return executeRequest(request, tClass);
    }

    public <T> T post(String url, Object body, Class<T> tClass) {
        String jsonBody = serializeRequestBody(body);
        Request request = buildRequest(url, null, "POST", jsonBody);
        return executeRequest(request, tClass);
    }

    private Request buildRequest(String url, HttpUrl.Builder urlBuilder, String method, String jsonBody) {
        if (urlBuilder == null) {
            urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        }

        Request.Builder requestBuilder = new Request.Builder();
        authProvider.applyAuth(requestBuilder, urlBuilder);

        Request.Builder finalRequestBuilder = requestBuilder.url(urlBuilder.build());
        if ("POST".equalsIgnoreCase(method) && jsonBody != null) {
            finalRequestBuilder.post(okhttp3.RequestBody.create(jsonBody, okhttp3.MediaType.parse("application/json")));
        }
        return finalRequestBuilder.build();
    }

    private <T> T executeRequest(Request request, Class<T> tClass) {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }
            if (response.body() == null) {
                throw new IOException("Response body is null");
            }
            return objectMapper.readValue(response.body().string(), tClass);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute request", e);
        }
    }

    private String serializeRequestBody(Object body) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to serialize request body to JSON", e);
        }
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
