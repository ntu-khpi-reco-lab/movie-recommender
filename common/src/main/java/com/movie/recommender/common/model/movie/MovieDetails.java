package com.movie.recommender.common.model.movie;

import lombok.Data;
import java.util.List;

@Data
public class MovieDetails {
    // Properties previously in the Movie class
    private Long id;
    private String title;
    private String overview;
    private String releaseDate;
    private Double rating;
    private Integer runtime;
    private boolean adult;

    // Direct lists for cast, crew, and keywords
    private List<MovieCredits.CastMember> cast;
    private List<MovieCredits.CrewMember> crew;
    private List<MovieKeywords.Keyword> keywords;
}