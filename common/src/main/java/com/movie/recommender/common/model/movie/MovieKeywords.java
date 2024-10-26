package com.movie.recommender.common.model.movie;

import lombok.Data;
import java.util.List;

@Data
public class MovieKeywords {
    private int id;
    private List<Keyword> keywords;

    @Data
    public static class Keyword {
        private int id;
        private String name;
    }
}
