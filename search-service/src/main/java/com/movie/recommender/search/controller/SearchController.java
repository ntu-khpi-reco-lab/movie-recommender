package com.movie.recommender.search.controller;

import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.search.exception.RatingError;
import com.movie.recommender.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/byTitle")
    public ResponseEntity<List<MovieDetails>> getMoviesByTitle(@RequestParam(value = "title") String title) {
        log.info("Search request for title: {}", title);
        List<MovieDetails> movies = searchService.getMovieByTitle(title);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/byGenres")
    public ResponseEntity<List<MovieDetails>> getMoviesByGenre(@RequestParam(value = "genres") List<String> genres) {
        log.info("Search request for genres: {}", genres);
        List<MovieDetails> movies = searchService.getMovieByGenres(genres);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/byReleaseDate")
    public ResponseEntity<Object> getMoviesByReleaseDate(@RequestParam(value = "startDate") String startDate,
                                                         @RequestParam(value = "endDate") String endDate) {
        log.info("Search request for release date from {} to {}", startDate, endDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start;
        LocalDate end;

        try {
            start = LocalDate.parse(startDate, formatter);
            end = LocalDate.parse(endDate, formatter);
        } catch (DateTimeParseException e) {
            log.error("Invalid date format: startDate={}, endDate={}", startDate, endDate);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format. Use 'yyyy-MM-dd'.");
        }

        if (start.isAfter(end)) {
            log.error("Start date {} is after end date {}", start, end);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Start date must be before or equal to end date.");
        }

        List<MovieDetails> movies = searchService.getMovieByReleaseDate(startDate, endDate);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/byRating")
    public ResponseEntity<Object> getMoviesByRating(@RequestParam(value = "ratingStart") Double ratingStart,
                                                    @RequestParam(value = "ratingEnd") Double ratingEnd) {
        log.info("Search request for rating from {} to {}", ratingStart, ratingEnd);
        if (ratingStart < 0 || ratingEnd < 0 || ratingStart > ratingEnd) {
            RatingError error = new RatingError();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        List<MovieDetails> movies = searchService.getMoviesByRating(ratingStart, ratingEnd);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/adultMovies")
    public ResponseEntity<List<MovieDetails>> getAdultMovies() {
        log.info("Search request for adult movies");
        List<MovieDetails> movies = searchService.getAdultMovies();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/nonAdultMovies")
    public ResponseEntity<List<MovieDetails>> getNonAdultMovies() {
        log.info("Search request for non-adult movies");
        List<MovieDetails> movies = searchService.getNonAdultMovies();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/byCastNames")
    public ResponseEntity<List<MovieDetails>> getMoviesByCastNames(@RequestParam(value = "names") List<String> names) {
        log.info("Search request for cast member names: {}", names);
        List<MovieDetails> movies = searchService.getMoviesByCastMemberNames(names);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/byCrewNames")
    public ResponseEntity<List<MovieDetails>> getMoviesByCrewNames(@RequestParam(value = "names") List<String> names) {
        log.info("Search request for crew member names: {}", names);
        List<MovieDetails> movies = searchService.getMoviesByCrewMemberNames(names);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/byKeywords")
    public ResponseEntity<List<MovieDetails>> getMoviesByKeywords(@RequestParam(value = "keywords") List<String> keywords) {
        log.info("Search request for keywords: {}", keywords);
        List<MovieDetails> movies = searchService.getMoviesByKeywords(keywords);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }
}
