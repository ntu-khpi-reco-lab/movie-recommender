package com.movie.recommender.common.model.movie;
import lombok.Data;


@Data
public class MovieAllData {
    private Movie movie;
    private MovieCredits movieCredits;
    private MovieKeywords movieKeywords;
}
