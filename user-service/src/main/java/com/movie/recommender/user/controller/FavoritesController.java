package com.movie.recommender.user.controller;

import com.movie.recommender.user.model.dto.FavoriteMoviesDTO;
import com.movie.recommender.user.service.FavoritesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/favorites")
public class FavoritesController {

    private final FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Set<Long>> getFavoriteMovies(@PathVariable("userId") Long userId) {
        Set<Long> favoriteMovies = favoritesService.getFavoriteMovies(userId);
        return ResponseEntity.ok(favoriteMovies);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Void> addFavoriteMovies(@PathVariable("userId") Long userId, @RequestBody FavoriteMoviesDTO favoriteMoviesDTO) {
        favoritesService.addFavoriteMovies(userId, favoriteMoviesDTO.getMovieIds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeFavoriteMovies(@PathVariable("userId") Long userId, @RequestBody FavoriteMoviesDTO favoriteMoviesDTO) {
        favoritesService.removeFavoriteMovies(userId, favoriteMoviesDTO.getMovieIds());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/{movieId}")
    public ResponseEntity<Void> removeFavoriteMovie(@PathVariable("userId") Long userId, @PathVariable("movieId") Long movieId) {
        favoritesService.removeFavoriteMovie(userId, movieId);
        return ResponseEntity.noContent().build();
    }
}
