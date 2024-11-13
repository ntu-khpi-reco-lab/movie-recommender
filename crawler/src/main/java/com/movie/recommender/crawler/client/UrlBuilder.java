package com.movie.recommender.crawler.client;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlBuilder {
    private static final String BASE_TMDB_URL = "https://api.themoviedb.org/3";
    private static final String BASE_SERPAPI_URL = "https://serpapi.com/";

    public static String movieDetailsUrl(String movieId) {
        return BASE_TMDB_URL + "/movie/" + movieId;
    }

    public static String nowPlayingMoviesUrl(String region) {
        return BASE_TMDB_URL + "/movie/now_playing?region=" + encode(region);
    }

    public static String movieCreditsUrl(String movieId) {
        return BASE_TMDB_URL + "/movie/" + movieId + "/credits";
    }

    public static String movieKeywordsUrl(String movieId) {
        return BASE_TMDB_URL + "/movie/" + movieId + "/keywords";
    }

    public static String movieShowtimesUrl(String movieName,String location, String language) {
        return BASE_SERPAPI_URL + "search.json?q=" +
                encode(movieName)+ "+showtimes&location=" +
                encode(location) + "&hl=" + language;
    }

    private static String encode(String input) {
        return URLEncoder.encode(input, StandardCharsets.UTF_8);
    }
}
