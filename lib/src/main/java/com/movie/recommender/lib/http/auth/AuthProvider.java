package com.movie.recommender.lib.http.auth;

import okhttp3.HttpUrl;
import okhttp3.Request;

public interface AuthProvider {
    void applyAuth(Request.Builder requestBuilder, HttpUrl.Builder urlBuilder);
}