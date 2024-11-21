package com.movie.recommender.search.model;

public interface RatingFilter {
    public final double MINIMAL_RATING = 0.0;
    public final double MAXIMAL_RATING = 10.0;
    public void validateRating();
}
