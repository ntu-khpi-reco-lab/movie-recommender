package com.movie.recommender.crawler.controller;

import com.movie.recommender.common.model.showtime.ShowtimesByCity;
import com.movie.recommender.crawler.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/crawl")
@Slf4j
public class CrawlerController {
    private final MovieService movieService;

    public CrawlerController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<String> triggerCrawl() {
        try {
            log.info("Manual crawl process initiated.");
            movieService.processCountryMovies();
            return ResponseEntity.ok("Crawl process started successfully.");
        } catch (Exception e) {
            log.error("Error during crawl process: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("An error occurred during the crawl process.");
        }
    }

    @GetMapping("/showtimes")
    public ResponseEntity<ShowtimesByCity> getShowtimesByLocation(
            @RequestParam("country") String country,
            @RequestParam("city") String city) {

            ShowtimesByCity showtimes = movieService.getShowtimesByCity(country, city);

            return ResponseEntity.ok(showtimes);
    }

}