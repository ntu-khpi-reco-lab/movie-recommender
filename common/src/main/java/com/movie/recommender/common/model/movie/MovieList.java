package com.movie.recommender.common.model.movie;

import lombok.Data;

import java.util.List;

@Data
public class MovieList {
    private List<Movie> results;
    private Dates dates;
    private int page;
    private int totalResults;
    private int totalPages;

    @Data
    public static class Dates {
        private String maximum;
        private String minimum;
    }
}