package com.movie.recommender.crawler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Films {
    private static final Logger logger = LoggerFactory.getLogger(Films.class);
    private static final String TOKEN = "Bearer YOUR_TOKEN";
    private final int pages;
    private List<JSONObject> films;
    private List<JSONObject> genres;

    public Films(int pages) {
        this.pages = pages;
        this.films = new ArrayList<>();
        this.genres = new ArrayList<>();
        fetchData();
        fetchGenres();
    }

    private void fetchData() {
        logger.info("Загрузка фильмов, страниц: {}", this.pages);
        for (int i = 1; i <= pages; i++) {
            try {
                URL url = new URL("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&page=" + i);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", TOKEN);
                connection.setRequestProperty("accept", "application/json");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                JSONObject response = new JSONObject(content.toString());
                JSONArray results = response.getJSONArray("results");
                for (int j = 0; j < results.length(); j++) {
                    films.add(results.getJSONObject(j));
                }

                connection.disconnect();
            } catch (Exception e) {
                logger.error("Ошибка при загрузке данных для страницы {}", i, e);
            }
        }
    }

    private void fetchGenres() {
        logger.info("Загрузка жанров");
        try {
            URL url = new URL("https://api.themoviedb.org/3/genre/movie/list");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", TOKEN);
            connection.setRequestProperty("accept", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            JSONObject response = new JSONObject(content.toString());
            genres.add(response);

            connection.disconnect();
        } catch (Exception e) {
            logger.error("Ошибка при загрузке жанров", e);
        }
    }

    public List<JSONObject> getAllData() {
        return this.films;
    }
}
