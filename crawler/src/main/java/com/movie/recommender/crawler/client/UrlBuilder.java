package com.movie.recommender.crawler.client;

public class UrlBuilder {
    private static final String BASE_TMDB_URL = "https://api.themoviedb.org/3";

    public static String movieDetailsUrl(String movieId) {
        return BASE_TMDB_URL + "/movie/" + movieId;
    }

    public static String nowPlayingMoviesUrl() {
        return BASE_TMDB_URL + "/movie/now_playing";
    }

    public static String movieCreditsUrl(String movieId) {
        return BASE_TMDB_URL + "/movie/" + movieId + "/credits";
    }

    public static String movieKeywordsUrl(String movieId) {
        return BASE_TMDB_URL + "/movie/" + movieId + "/keywords";
    }

}
