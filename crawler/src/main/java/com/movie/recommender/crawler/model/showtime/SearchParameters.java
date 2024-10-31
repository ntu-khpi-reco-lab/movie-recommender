package com.movie.recommender.crawler.model.showtime;

import lombok.Data;

@Data
public class SearchParameters {
    private String engine;
    private String q;
    private String locationRequested;
    private String locationUsed;
    private String googleDomain;
    private String hl;
    private String device;
}