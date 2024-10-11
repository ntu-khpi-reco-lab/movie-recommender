package com.movie.recommender.crawler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hw.serpapi.GoogleSearch;
import com.hw.serpapi.SerpApiSearchException;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SerpApiShowtimes {
    private static final String API_KEY = "your_serpapi_key"; // Replace with your SerpAPI key
    private static final String showtime_file_path = "crawler/src/main/java/resources/static_showtime.txt";
    public String location;
    public String language;
    public String region;
    public String cinema;
    public JsonElement showtimes_data;
    public SerpApiShowtimes(String location, String language, String region, String cinema) {
        this.location = location;
        this.language = language;
        this.region = region;
        this.cinema = cinema;
        showtimes_data = readJsonFromFile(showtime_file_path);
    }



    public void fetchData(){
        Map<String, String> parameter = new HashMap<>();

        parameter.put("q", this.cinema);
        parameter.put("location", this.location );
        parameter.put("hl", this.language);
        parameter.put("gl", this.region);
        parameter.put("api_key", API_KEY );

        GoogleSearch search = new GoogleSearch(parameter);

        try {
            JsonObject results = search.getJson();
            this.showtimes_data = results.get("showtimes");
        } catch (SerpApiSearchException ex) {
            System.out.println("Exception:");
            System.out.println(ex.toString());
        }
    }

    public static JsonElement readJsonFromFile(String filePath) {
        // Create a Gson instance
        Gson gson = new Gson();
        JsonElement jsonElement = null;
        try {
            // Reading the file into a FileReader
            FileReader reader = new FileReader(filePath);

            // Parsing the JSON from the file into a JsonElement
            jsonElement = JsonParser.parseReader(reader);

            // Close the file reader
            reader.close();

            // Return the JsonElement
            return jsonElement;

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return null if there was an issue reading the file
        return jsonElement;
    }
}
