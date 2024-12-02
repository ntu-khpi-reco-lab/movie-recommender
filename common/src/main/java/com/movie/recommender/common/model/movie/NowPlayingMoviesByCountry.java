package com.movie.recommender.common.model.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class NowPlayingMoviesByCountry {
    private String countryCode;
    private String countryName;
    @JsonProperty("dates")
    private DateRange dateRange;
    private List<MovieIdentifier> results;

    @Data
    public static class DateRange {
        @JsonProperty("minimum")
        private String startDate;
        @JsonProperty("maximum")
        private String endDate;
    }

    @Data
    public static class MovieIdentifier {
        private Long id;
        private String title;
    }
}

