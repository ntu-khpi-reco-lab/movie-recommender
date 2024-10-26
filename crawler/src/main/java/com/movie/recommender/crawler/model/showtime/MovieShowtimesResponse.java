package com.movie.recommender.crawler.model.showtime;

import com.movie.recommender.common.model.showtime.Showtime;
import lombok.Data;

import java.util.List;

@Data
public class MovieShowtimesResponse {

    private SearchMetadata searchMetadata;
    private SearchParameters searchParameters;
    private SearchInformation searchInformation;
    private List<Showtime> showtimes;
}