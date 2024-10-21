package com.movie.recommender.crawler;


import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Films films = new Films(1);
        System.out.println(films.ongoing_film_results_array.size());
        System.out.println(films.getFilmNames(films.ongoing_film_results_array).size());
        String language = "en";
        String search_region = "us";
        SerpApiShowtimes showtimes = new SerpApiShowtimes(language, search_region);
        System.out.println(showtimes.showtimes_data.getAsJsonArray());
        List<String> film_names = new ArrayList<>();
        List<String> locations = new ArrayList<>();
        film_names.add("Joker 2");
        locations.add("Austin, Texas, United States");
        DB db = new DB();
        db.dbConnect("test");
        CsvWorker csv = new CsvWorker();
        csv.csvLoader("crawler/src/main/java/resources/tbd_data.csv", films.film_results_array.toString());



    }
}