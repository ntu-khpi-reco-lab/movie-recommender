package com.movie.recommender.common.model.movie;

import lombok.Data;
import java.util.List;

@Data
public class NowPlayingMovieDetail {
    private Long id;
    private String title;
    private String releaseDate;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private double popularity;
    private double voteAverage;
    private int voteCount;
    private List<Movie.Genre> genres;
}