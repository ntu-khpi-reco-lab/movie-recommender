package com.movie.recommender.crawler.controller;

import com.movie.recommender.crawler.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class CrawlerController {

    private final MovieService movieService;

    public CrawlerController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/api/v1/internal/crawl")
    public ResponseEntity<String> triggerCrawl() {
        try {
            log.info("Manual crawl process initiated.");

            movieService.processCountryMovies();

            log.info("Crawl process completed successfully.");
            return ResponseEntity.ok("Crawl process completed successfully.");
        } catch (Exception e) {
            log.error("Error during crawl process: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("An error occurred during the crawl process.");
        }
    }
}