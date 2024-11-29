package com.movie.recommender.user.controller;

import com.movie.recommender.user.model.dto.FavoriteMoviesDTO;
import com.movie.recommender.user.service.FavoritesService;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping()
    public ResponseEntity<Set<Long>> getFavoriteMovies(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Set<Long> favoriteMovies = favoritesService.getFavoriteMovies(userId);
        return ResponseEntity.ok(favoriteMovies);
    }

    @PostMapping()
    public ResponseEntity<Void> addFavoriteMovies(HttpServletRequest request, @RequestBody FavoriteMoviesDTO favoriteMoviesDTO) {
        Long userId = (Long) request.getAttribute("userId");
        favoritesService.addFavoriteMovies(userId, favoriteMoviesDTO.getMovieIds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> removeFavoriteMovies(HttpServletRequest request, @RequestBody FavoriteMoviesDTO favoriteMoviesDTO) {
        Long userId = (Long) request.getAttribute("userId");
        favoritesService.removeFavoriteMovies(userId, favoriteMoviesDTO.getMovieIds());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> removeFavoriteMovie(HttpServletRequest request, @PathVariable("movieId") Long movieId) {
        Long userId = (Long) request.getAttribute("userId");
        favoritesService.removeFavoriteMovie(userId, movieId);
        return ResponseEntity.noContent().build();
    }
}
