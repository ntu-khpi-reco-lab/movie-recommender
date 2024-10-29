package com.movie.recommender.common.model.movie;

import lombok.Data;

import java.util.List;

@Data
public class JsonData {
    private List<Movie> detail;
    private List<MovieCredits> credit;
    private List<MovieKeywords> keyword;
}