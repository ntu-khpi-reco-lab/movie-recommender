package com.movie.recommender.search.exception;

public class RatingError {
    private String message;
    private int status;

    public RatingError() {
        this.message = "Invalid rating range. Ensure ratingStart >= 0, ratingEnd >= 0, and ratingStart <= ratingEnd.";
        ;
        this.status = 404;
    }
}
