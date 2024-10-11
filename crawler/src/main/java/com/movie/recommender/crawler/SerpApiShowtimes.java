package com.movie.recommender.crawler;

import com.hw.serpapi.GoogleSearch;
import com.hw.serpapi.SerpApiSearchException;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;

public class SerpApiShowtimes {

    public void fetchData(){
        Map<String, String> parameter = new HashMap<>();

        parameter.put("q", "AMC Barton Creek Square 14");
        parameter.put("location", "Austin, Texas, United States");
        parameter.put("hl", "en");
        parameter.put("gl", "us");
        parameter.put("api_key", "secret_api_key");

        GoogleSearch search = new GoogleSearch(parameter);

        try {
            JsonObject results = search.getJson();
            var showtimes = results.get("showtimes");
        } catch (SerpApiSearchException ex) {
            System.out.println("Exception:");
            System.out.println(ex.toString());
        }
    }
}
