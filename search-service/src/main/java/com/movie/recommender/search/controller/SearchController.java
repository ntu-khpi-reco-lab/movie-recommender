package com.movie.recommender.search.controller;

import com.movie.recommender.search.model.SearchFilter;
import com.movie.recommender.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity<Object> findMovies(@ModelAttribute SearchFilter filter) {
        try {
            return  new ResponseEntity<>(searchService.search(filter), HttpStatus.OK) ;
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
