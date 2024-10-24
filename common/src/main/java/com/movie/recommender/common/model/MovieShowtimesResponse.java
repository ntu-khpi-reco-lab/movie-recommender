package com.movie.recommender.common.model;

import lombok.Data;

import java.util.List;

@Data
public class MovieShowtimesResponse {

    private SearchMetadata searchMetadata;
    private SearchParameters searchParameters;
    private SearchInformation searchInformation;
    private List<Showtime> showtimes;

    @Data
    public static class SearchMetadata {
        private String id;
        private String status;
        private String jsonEndpoint;
        private String createdAt;
        private String processedAt;
        private String googleUrl;
        private String rawHtmlFile;
        private double totalTimeTaken;
    }

    @Data
    public static class SearchParameters {
        private String engine;
        private String q;
        private String locationRequested;
        private String locationUsed;
        private String googleDomain;
        private String hl;
        private String device;
    }

    @Data
    public static class SearchInformation {
        private String queryDisplayed;
        private int totalResults;
        private double timeTakenDisplayed;
        private String organicResultsState;
    }

    @Data
    public static class Showtime {
        private String day;
        private List<Theater> theaters;
    }

    @Data
    public static class Theater {
        private String name;
        private String link;
        private String distance;
        private String address;
        private List<Showing> showing;
    }

    @Data
    public static class Showing {
        private List<String> time;
        private String type;
    }
}