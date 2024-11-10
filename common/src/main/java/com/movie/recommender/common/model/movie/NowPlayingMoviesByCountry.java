package com.movie.recommender.common.model.movie;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
public class NowPlayingMoviesByCountry {
    private String country;
    private Dates dates;
    private List<NowPlayingMovieDetail> results;


    @Data
    public static class Dates {
        private String maximum;
        private String minimum;
    }
}

