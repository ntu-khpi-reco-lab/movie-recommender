package com.movie.recommender.crawler;

public class Main {
    public static void main(String[] args) {
        Films films = new Films(10);
        System.out.println(films.film_results_array);
        films.fetchGenres();
        System.out.println(films.genres);
    }
}