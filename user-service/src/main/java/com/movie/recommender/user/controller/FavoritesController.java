package com.movie.recommender.user.controller;

import com.movie.recommender.user.model.dto.FavoriteMoviesDTO;
import com.movie.recommender.user.service.FavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/users/{userId}/favorites")
public class FavoritesController {

    private final FavoritesService favoritesService;

    @Autowired
    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @GetMapping
    public ResponseEntity<Set<Long>> getFavoriteMovies(@PathVariable("userId") Long userId) {
        Set<Long> favoriteMovies = favoritesService.getFavoriteMovies(userId);
        return ResponseEntity.ok(favoriteMovies);
    }

    @PostMapping
    public ResponseEntity<Void> addFavoriteMovies(@PathVariable("userId") Long userId, @RequestBody FavoriteMoviesDTO favoriteMoviesDTO) {
        favoritesService.addFavoriteMovies(userId, favoriteMoviesDTO.getMovieIds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavoriteMovies(@PathVariable("userId") Long userId, @RequestBody FavoriteMoviesDTO favoriteMoviesDTO) {
        favoritesService.removeFavoriteMovies(userId, favoriteMoviesDTO.getMovieIds());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> removeFavoriteMovie(@PathVariable("userId") Long userId, @PathVariable Long movieId) {
        favoritesService.removeFavoriteMovie(userId, movieId);
        return ResponseEntity.noContent().build();
    }
}
