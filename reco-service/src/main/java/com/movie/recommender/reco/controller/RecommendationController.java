package com.movie.recommender.reco.controller;


import com.movie.recommender.reco.client.FavoritesClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/recommend")
public class RecommendationController {
    private final FavoritesClient favoritesClient;

    public RecommendationController(FavoritesClient favoritesClient) {
        this.favoritesClient = favoritesClient;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Set<Long>> recommend(@PathVariable("userId") Long userId) {
        try {
            // Вытягиваем список фильмов, полайканных пользователем
            Set<Long> favoriteMovies = favoritesClient.getFavoriteMovies(userId);
            return ResponseEntity.ok(favoriteMovies);
        } catch (Exception e) {
            // Логируем ошибку и возвращаем ответ с ошибкой
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
