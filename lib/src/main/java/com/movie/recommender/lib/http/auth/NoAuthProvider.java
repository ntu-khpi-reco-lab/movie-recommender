package com.movie.recommender.lib.http.auth;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class NoAuthProvider implements AuthProvider {
    /**
     * This method is intentionally left empty because NoAuthProvider represents a case where
     * no authentication is required for the requests. By implementing the AuthProvider interface
     * but leaving applyAuth() empty, we ensure that no authentication details are added to the
     * request, allowing it to bypass any auth mechanisms.
     */
    @Override
    public void applyAuth(Request.Builder requestBuilder, HttpUrl.Builder urlBuilder) {}
}
