package com.movie.recommender.crawler;


import com.movie.recommender.crawler.client.SerpApiClient;
import com.movie.recommender.crawler.client.UrlBuilder;

public class Main {
    public static void main(String[] args) {
        System.out.println("Crawler started");

        SerpApiClient client = new SerpApiClient();
        System.out.println( client.getMovieShowtimes("The wild robot","Austin, Texas, United States","en"));
    }
}
