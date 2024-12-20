package com.movie.recommender.reco.controller;

import com.movie.recommender.common.model.reco.MovieWithShowtime;
import com.movie.recommender.reco.service.RecommendationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommend")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public ResponseEntity<List<MovieWithShowtime>> recommend(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            List<MovieWithShowtime> recommendations = recommendationService.getRecommendations(token);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}