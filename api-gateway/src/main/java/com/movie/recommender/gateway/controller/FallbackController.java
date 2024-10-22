package com.movie.recommender.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback")
    public String fallback() {
        return "The service is unavailable at the moment. Please try again later.";
    }

    @GetMapping("/locations")
    public ResponseEntity<String> locationServiceFallback() {
        return new ResponseEntity<>("Location Service is currently unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
