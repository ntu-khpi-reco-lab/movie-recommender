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

    @GetMapping("/users")
    public ResponseEntity<String> userServiceFallback() {
        return new ResponseEntity<>("User Service is currently unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchServiceFallback() {
        return new ResponseEntity<>("Search Service is currently unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/recommend")
    public ResponseEntity<String> recoServiceFallback() {
        return new ResponseEntity<>("Recommendation Service is currently unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
