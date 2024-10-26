package com.movie.recommender.lib.http.auth;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class BearerAuthProvider implements AuthProvider {
    private final String authToken;

    public BearerAuthProvider(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public void applyAuth(Request.Builder requestBuilder, HttpUrl.Builder urlBuilder) {
        if (authToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }
    }
}

