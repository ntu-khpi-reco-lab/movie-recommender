package com.movie.recommender.crawler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class Films {
    private static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzMTI3NGFmYTRlNTUyMjRjYzRlN2Q0NmNlMTNkOTZjOSIsInN1YiI6IjVkNmZhMWZmNzdjMDFmMDAxMDU5NzQ4OSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.lbpgyXlOXwrbY0mUmP-zQpNAMCw_h-oaudAJB6Cn5c8";
    private static String popular_movies_url = "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&page=";
    private final int pages;
    public JsonArray film_results_array = new JsonArray();
    public JsonObject film_results;
    public JsonArray genres;
    public Films(int pages) {
        this.pages = pages;
        fetchDataForPages();
        fetchGenres();
    }

    private JsonArray fetchData(int page,String question_url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(question_url+page)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", TOKEN)
                .build();


        JsonArray result = new JsonArray();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonData = response.body().string();
                this.film_results = JsonParser.parseString(jsonData).getAsJsonObject();
                result = film_results.getAsJsonArray("results");
            } else {
                System.out.println("Error: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    private void fetchDataForPages(){

        for (int i = 1; i < pages; i++) {
            this.film_results_array.addAll(fetchData(i,popular_movies_url));
        }
    }

    public void fetchGenres() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()

                .url("https://api.themoviedb.org/3/genre/movie/list")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", TOKEN)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonData = response.body().string();
                JsonObject object = JsonParser.parseString(jsonData).getAsJsonObject();
                this.genres = object.getAsJsonArray("genres");
            } else {
                System.out.println("Error: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
