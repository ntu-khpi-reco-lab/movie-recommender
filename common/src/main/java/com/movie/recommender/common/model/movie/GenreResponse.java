package com.movie.recommender.common.model.movie;

import lombok.Data;
import java.util.List;

@Data
public class GenreResponse {
    private List<Movie.Genre> genres;
}