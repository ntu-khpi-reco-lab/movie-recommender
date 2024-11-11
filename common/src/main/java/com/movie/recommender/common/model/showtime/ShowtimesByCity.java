package com.movie.recommender.common.model.showtime;

import lombok.Data;
import java.util.List;

@Data
public class ShowtimesByCity {
    private String countryCode;
    private String cityName;
    private List<MovieIdentifier> movies;


    @Data
    public static class MovieIdentifier {
        private Long id;
        private String title;
        private List<Showtime> showtimes;
    }
}