package com.movie.recommender.common.model.movie;

import lombok.Data;
import java.util.List;

@Data
public class NowPlayingMoviesByCountry {
    private String countryCode;
    private DateRange dateRange;
    private List<MovieIdentifier> results;

    @Data
    public static class DateRange {
        private String startDate;
        private String endDate;
    }

    @Data
    public static class MovieIdentifier {
        private Long id;
        private String title;
    }
}

