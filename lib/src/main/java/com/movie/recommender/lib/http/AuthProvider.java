package com.movie.recommender.lib.http;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class AuthProvider {
    private final String authToken;
    private final String apiKey;

    public AuthProvider(String authToken, String apiKey) {
        this.authToken = authToken;
        this.apiKey = apiKey;
    }

    public void addAuthHeader(Request.Builder requestBuilder) {
        if (authToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }
    }

    public HttpUrl addApiKey(HttpUrl url) {
        if (apiKey != null) {
            return url.newBuilder().addQueryParameter("api_key", apiKey).build();
        }
        return url;
    }
}
