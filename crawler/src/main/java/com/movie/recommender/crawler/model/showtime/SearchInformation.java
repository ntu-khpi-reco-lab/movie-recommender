package com.movie.recommender.crawler.model.showtime;

import lombok.Data;

@Data
public class SearchInformation {
    private String queryDisplayed;
    private int totalResults;
    private double timeTakenDisplayed;
    private String organicResultsState;
}