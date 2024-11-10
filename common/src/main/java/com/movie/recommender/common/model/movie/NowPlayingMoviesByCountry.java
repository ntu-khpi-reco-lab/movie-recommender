package com.movie.recommender.common.model.movie;

import lombok.Data;

import java.util.List;

@Data
public class NowPlayingMoviesByCountry {
    private String country;
    private Dates dates;
    private List<MoviesIdResult> results;


    @Data
    public static class Dates {
        private String maximum;
        private String minimum;
    }

    @Data
    public static class MoviesIdResult {
        private Long id;
        private String title;
    }


}

