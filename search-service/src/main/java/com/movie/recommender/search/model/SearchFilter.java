package com.movie.recommender.search.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Data
public class SearchFilter implements RatingFilter, DateFilter, TitleFilter, GenresFilter, CastNamesFilter, CrewNamesFilter {
    private String movieTitle;
    private List<String> genres;
    private String startDate;
    private String endDate;
    private Double ratingStart;
    private Double ratingEnd;
    private Boolean isAdult;
    private List<String> castNames;
    private List<String> crewNames;
    private List<String> keywords;

    @Override
    public void validateRating() {
        if (ratingStart > ratingEnd) {
            throw new IllegalArgumentException("ratingStart must be lower than ratingEnd");
        }
        if (ratingStart < MINIMAL_RATING){
            throw new IllegalArgumentException("ratingStart must be greater than " + MINIMAL_RATING);
        }
        if (ratingEnd > MAXIMAL_RATING){
            throw new IllegalArgumentException("ratingEnd must be lower than " + MAXIMAL_RATING);
        }
    }

    @Override
    public void validateDate() {
        if (startDate.trim().isEmpty()) {
            throw new IllegalArgumentException("startDate cannot be empty");
        }
        if (endDate.trim().isEmpty()) {
            throw new IllegalArgumentException("endDate cannot be empty");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate dateStart = LocalDate.parse(startDate, formatter);
            LocalDate dateEnd = LocalDate.parse(endDate, formatter);

            if (dateStart.isAfter(dateEnd)) {
                throw new IllegalArgumentException("startDate must be before endDate");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format " + startDate + " and " + endDate);
        }
    }

    @Override
    public void validateMovieTitle(){
        if (movieTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("movieTitle is empty");
        }
        if (!movieTitle.matches("^[\\p{L}0-9 .,'!?&-]+$")) {
            throw new IllegalArgumentException("Title contains invalid characters");
        }
    }

    @Override
    public void validateGenres() {
        for (String genre : genres) {
            if (genre.trim().isEmpty()) {
                throw new IllegalArgumentException("Genre is empty");
            }
            if (!DEFAULT_GENRES.contains(genre)) {
                throw new IllegalArgumentException("Genre " + genre + " is not a valid genre");
            }
        }
    }

    @Override
    public void validateCastNames() {
        for (String name : castNames) {
            if (name.trim().isEmpty()) {
                throw new IllegalArgumentException("Cast name is empty");
            }
            if (!name.matches("^[\\p{L} .'-]+$")) {
                throw new IllegalArgumentException("The name " + name + " contains invalid characters.");
            }
        }
    }

    @Override
    public void validateCrewNames() {
        for (String name : crewNames) {
            if (name.trim().isEmpty()) {
                throw new IllegalArgumentException("Crew name is empty");
            }
            if (!name.matches("^[\\p{L} .'-]+$")) {
                throw new IllegalArgumentException("The name " + name + " contains invalid characters.");
            }
        }
    }
}
