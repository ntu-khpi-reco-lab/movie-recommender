package com.movie.recommender.crawler;

public class Main {
    public static void main(String[] args) {
        String location = "Austin, Texas, United States";
        String language = "en";
        String search_region = "us";
        String cinema = "AMC Barton Creek Square 14";
        SerpApiShowtimes showtimes = new SerpApiShowtimes(location, language, search_region, cinema);
        System.out.println(showtimes.showtimes_data);
        showtimes.fetchData();
        System.out.println(showtimes.showtimes_data);
    }
}