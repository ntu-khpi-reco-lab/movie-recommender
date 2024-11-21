package com.movie.recommender.search.service;

import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.search.model.SearchFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@Service
public class SearchService {
    final private MongoTemplate mongoTemplate;
    final private List<String> DEFAULT_GENRES = List.of(
            "Action",
            "Adventure",
            "Animation",
            "Comedy",
            "Crime",
            "Documentary",
            "Drama",
            "Family",
            "Fantasy",
            "History",
            "Horror",
            "Music",
            "Mystery",
            "Romance",
            "Science Fiction",
            "TV Movie",
            "Thriller",
            "War",
            "Western"

    );
    private final double MINIMAL_RATING = 0.0;
    private final double MAXIMAL_RATING = 10.0;

    public SearchService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<MovieDetails> search(SearchFilter filter) {
        Query query = new Query();

        if (filter.getMovieTitle() != null) {
            query.addCriteria(Criteria.where("title").regex(filter.getMovieTitle(), "i"));
            log.info("Filtering by movie title: {}", filter.getMovieTitle());
        }

        if (filter.getGenres() != null && !filter.getGenres().isEmpty()) {
            validateGenres(filter.getGenres());
            query.addCriteria(Criteria.where("genres.name").all(filter.getGenres()));
            log.info("Filtering by genres: {}", filter.getGenres());
        }

        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            validateDate(filter.getStartDate(), filter.getEndDate());
            query.addCriteria(Criteria.where("releaseDate")
                    .gte(filter.getStartDate())
                    .lte(filter.getEndDate()));
            log.info("Filtering by release date range: {} - {}", filter.getStartDate(), filter.getEndDate());
        }

        if (filter.getRatingStart() != null && filter.getRatingEnd() != null) {
            validateRating(filter.getRatingStart(), filter.getRatingEnd());
            query.addCriteria(Criteria.where("rating")
                    .gte(filter.getRatingStart())
                    .lte(filter.getRatingEnd()));
            log.info("Filtering by rating range: {} - {}", filter.getRatingStart(), filter.getRatingEnd());
        }

        if (filter.getIsAdult() != null) {
            query.addCriteria(Criteria.where("adult").is(filter.getIsAdult()));
            log.info("Filtering by adult content: {}", filter.getIsAdult());
        }

        if (filter.getCastNames() != null && !filter.getCastNames().isEmpty()) {
            query.addCriteria(Criteria.where("cast.name").all(filter.getCastNames()));
            log.info("Filtering by cast names: {}", filter.getCastNames());
        }

        if (filter.getCrewNames() != null && !filter.getCrewNames().isEmpty()) {
            query.addCriteria(Criteria.where("crew.name").all(filter.getCrewNames()));
            log.info("Filtering by crew names: {}", filter.getCrewNames());
        }

        if (filter.getKeywords() != null && !filter.getKeywords().isEmpty()) {
            query.addCriteria(Criteria.where("keywords.name").all(filter.getKeywords()));
            log.info("Filtering by keywords: {}", filter.getKeywords());
        }

        List<MovieDetails> result = mongoTemplate.find(query, MovieDetails.class);
        log.info("Query executed, found {} movies", result.size());
        return result;
    }

    private void validateRating(Double ratingStart, Double ratingEnd) {
        if (ratingStart > ratingEnd) {
            throw new IllegalArgumentException("ratingStart must be lower than ratingEnd");
        }
        if (ratingStart < MINIMAL_RATING) {
            throw new IllegalArgumentException("ratingStart must be greater than " + MINIMAL_RATING);
        }
        if (ratingEnd > MAXIMAL_RATING) {
            throw new IllegalArgumentException("ratingEnd must be lower than " + MAXIMAL_RATING);
        }
    }

    private void validateDate(String startDate, String endDate) {
        if (startDate.trim().isEmpty() || endDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Dates cannot be empty");
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

    private void validateGenres(List<String> genres) {
        for (String genre : genres) {
            if (!DEFAULT_GENRES.contains(genre)) {
                throw new IllegalArgumentException("Genre " + genre + " is not a valid genre");
            }
        }
    }
}
