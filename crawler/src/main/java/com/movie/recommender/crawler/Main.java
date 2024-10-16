package com.movie.recommender.crawler;

public class Main {
    public static void main(String[] args) {
        Films films = new Films(1);
        System.out.println(films.ongoing_film_results_array.size());
        System.out.println(films.getFilmNames(films.ongoing_film_results_array).size());
        String location = "Austin, Texas, United States";
        String language = "en";
        String search_region = "us";
        String film_name = "Joker 2";
        SerpApiShowtimes showtimes = new SerpApiShowtimes(location, language, search_region, film_name);
        System.out.println(showtimes.showtimes_data);
    }
}