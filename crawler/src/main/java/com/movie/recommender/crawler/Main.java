package com.movie.recommender.crawler;


import com.movie.recommender.common.model.movie.MovieDetails;
import dataimport.MovieDataParser;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Crawler started");
        MovieDataParser parser = new MovieDataParser();
        List<MovieDetails> movies = parser.parseMovieData("C:\\3course_project\\data\\movies.json");
        movies.forEach(System.out::println);
        System.out.println(movies.size());
    }


}
