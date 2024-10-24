package com.movie.recommender.crawler.client;

public class UrlBuilder {
    private static final String BASE_TMDB_URL = "https://api.themoviedb.org/3";
    private static final String BASE_SERPAPI_URL = "https://serpapi.com/";

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

    public static String movieShowtimesUrl(String movieName,String location, String language) {
        return BASE_SERPAPI_URL + "search.json?q="+replaceSpaces(movieName)+ "+showtimes&location="
                + replaceSpaces(location) + "&hl=" + language ;
    }

    private static String replaceSpaces(String input) {
        return input.replace(" ", "+");
    }

}
