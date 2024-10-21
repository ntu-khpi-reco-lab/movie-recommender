package com.movie.recommender.crawler.client;

public class UrlBuilder {
    private static final String BASE_TMDB_URL = "https://api.themoviedb.org/3";

    public static String movieDetailsUrl(String movieId) {
        return BASE_TMDB_URL + "/movie/" + movieId;
    }
}
