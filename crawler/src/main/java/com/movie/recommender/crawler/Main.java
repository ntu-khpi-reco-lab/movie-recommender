package com.movie.recommender.crawler;


import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Films films = new Films(1);
        System.out.println(films.ongoing_film_results_array.size());
        List<String> film_names = films.getFilmNames(films.ongoing_film_results_array);
        System.out.println(films.getFilmNames(films.ongoing_film_results_array).size());

        String language = "en";
        String search_region = "us";
        SerpApiShowtimes showtimes = new SerpApiShowtimes(language, search_region);

        List<String> film_names_for_db = new ArrayList<>();
        List<String> locations = new ArrayList<>();
        film_names_for_db.add(film_names.get(0));
        locations.add("Austin, Texas, United States");

        //JsonArray showtimes_db_filler = showtimes.makeDBFiller(film_names_for_db, locations);

        CsvWorker csv = new CsvWorker();
        csv.csvLoader("crawler/src/main/java/resources/tbd_data.csv", films.film_results_array.toString());


        DB db = new DB();

        //db.insertJsonListToCollection("filmsCollection", films.film_results_array);
        //db.insertJsonListToCollection("showtimesCollection", showtimes_db_filler);

        // Close the connection when done
        db.close();


    }


}