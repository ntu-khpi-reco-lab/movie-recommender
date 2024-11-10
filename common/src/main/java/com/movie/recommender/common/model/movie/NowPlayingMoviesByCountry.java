package com.movie.recommender.common.model.movie;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "NowPlayingByCountry")
public class NowPlayingMoviesByCountry {
    @Id
    private String country;
    private String startDate;
    private String endDate;
    private List<NowPlayingMovieDetail> movies;
}