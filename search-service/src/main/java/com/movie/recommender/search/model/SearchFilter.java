package com.movie.recommender.search.model;

import lombok.Data;
import java.util.List;

@Data
public class SearchFilter {
    private String movieTitle;
    private List<String> genres;
    private String startDate;
    private String endDate;
    private Double ratingStart;
    private Double ratingEnd;
    private Boolean isAdult;
    private List<String> castNames;
    private List<String> crewNames;
    private List<String> keywords;
}
