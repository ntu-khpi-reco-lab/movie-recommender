package com.movie.recommender.common.model.showtime;

import lombok.Data;
import java.util.List;

@Data
public class ShowtimesByCity {
    private String countryCode;
    private String countryName;
    private String cityName;
    private List<MovieShowtimes> movies;


    @Data
    public static class MovieShowtimes {
        private Long id;
        private String title;
        private List<Showtime> showtimes;
    }
}