package com.movie.recommender.crawler;

import com.google.gson.*;
import com.hw.serpapi.GoogleSearch;
import com.hw.serpapi.SerpApiSearchException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerpApiShowtimes {
    private static final String API_KEY = "9dc14c3ae6901ba9fbc7e0257594425557b53a2399fda39f89ec67c074df9a56"; // Replace with your SerpAPI key
    private static final String showtime_file_path = "crawler/src/main/java/resources/static_showtime.txt";
    private static ZonedDateTime  time_of_created_request;
    public String dateString = "2024-10-16 21:01:51 UTC";
    public String language;
    public String region;
    public JsonElement showtimes_data;
    public JsonArray showtimes_data_for_db;
    public SerpApiShowtimes( String language, String region) {
        this.language = language;
        this.region = region;
    }

    private ZonedDateTime date_parse(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

        // Parse the date-time string to ZonedDateTime
        ZonedDateTime dateTime = ZonedDateTime.parse(dateString, formatter.withZone(ZoneId.of("UTC")));
        return dateTime;

    }

    public void fetchData(String location,String film_name){
        Map<String, String> parameter = new HashMap<>();
        parameter.put("q", film_name + " showtimes" );
        parameter.put("location", location );
        parameter.put("hl", this.language);
        parameter.put("gl", this.region);
        parameter.put("api_key", API_KEY );

        GoogleSearch search = new GoogleSearch(parameter);
        System.out.println(search);

        try {
            JsonObject results = search.getJson();
            System.out.println(results);
            JsonObject searchMetadata = results.getAsJsonObject("search_metadata");
            String processedAt = searchMetadata.get("processed_at").getAsString();
            this.time_of_created_request = date_parse(processedAt );

            this.showtimes_data = results.get("showtimes");
        } catch (SerpApiSearchException ex) {
            System.out.println("Exception:");
            System.out.println(ex.toString());
        }
    }

    public JsonArray makeDBFiller(List<String> film_names, List<String> location ) {

        JsonArray results = new JsonArray();
        String language = "en";
        String search_region = "us";
        for (int i = 0; i < location.size(); i++){
            for (int k = 0; k < film_names.size(); k++){
                SerpApiShowtimes showtimes = new SerpApiShowtimes( language, search_region );
                showtimes.fetchData(location.get(i),film_names.get(k));
                for (int d = 0; d < showtimes.showtimes_data.getAsJsonArray().size(); d++) {

                    String day = showtimes.showtimes_data.getAsJsonArray().get(d).getAsJsonObject().get("day").getAsString();
                    showtimes.showtimes_data.getAsJsonArray().get(d).getAsJsonObject().remove("day");
                    JsonObject day_data = showtimes.showtimes_data.getAsJsonArray().get(d).getAsJsonObject();

                    for (int theater = 0; theater < day_data.getAsJsonArray("theaters").size(); theater++){
                        day_data.getAsJsonArray("theaters").get(theater).getAsJsonObject().addProperty("day", time_of_created_request.plusDays(d).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        results.add(day_data.getAsJsonArray("theaters").get(theater));
                    }

                }
            }
        }
        System.out.println(results);
        this.showtimes_data_for_db = results;
        return results;


    }


}
