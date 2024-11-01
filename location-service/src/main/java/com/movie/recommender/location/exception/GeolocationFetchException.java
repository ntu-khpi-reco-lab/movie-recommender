package com.movie.recommender.location.exception;

public class GeolocationFetchException extends RuntimeException {
    public GeolocationFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
