package com.movie.recommender.lib.http.auth;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class ApiKeyAuthProvider implements AuthProvider {
    private final String apiKey;

    public ApiKeyAuthProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void applyAuth(Request.Builder requestBuilder, HttpUrl.Builder urlBuilder) {
        if (apiKey != null) {
            urlBuilder.addQueryParameter("api_key", apiKey);
        }
    }
}
