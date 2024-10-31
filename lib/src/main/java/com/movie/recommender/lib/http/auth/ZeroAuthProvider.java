package com.movie.recommender.lib.http.auth;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class ZeroAuthProvider implements AuthProvider {
    @Override
    public void applyAuth(Request.Builder requestBuilder, HttpUrl.Builder urlBuilder) {}
}
