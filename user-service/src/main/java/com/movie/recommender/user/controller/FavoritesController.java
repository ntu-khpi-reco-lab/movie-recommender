package com.movie.recommender.user.controller;

import com.movie.recommender.user.model.dto.FavoriteMoviesDTO;
import com.movie.recommender.common.security.CustomPrincipal;
import com.movie.recommender.user.service.FavoritesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/favorites")
public class FavoritesController {

    private final FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @GetMapping
    public ResponseEntity<Set<Long>> getFavoriteMovies(@AuthenticationPrincipal CustomPrincipal principal) {
        Set<Long> favoriteMovies = favoritesService.getFavoriteMovies(principal.getUserId());
        return ResponseEntity.ok(favoriteMovies);
    }

    @PostMapping
    public ResponseEntity<Void> addFavoriteMovies(@AuthenticationPrincipal CustomPrincipal principal,
                                                  @RequestBody FavoriteMoviesDTO favoriteMoviesDTO) {
        favoritesService.addFavoriteMovies(principal.getUserId(), favoriteMoviesDTO.getMovieIds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavoriteMovies(@AuthenticationPrincipal CustomPrincipal principal,
                                                     @RequestBody FavoriteMoviesDTO favoriteMoviesDTO) {
        favoritesService.removeFavoriteMovies(principal.getUserId(), favoriteMoviesDTO.getMovieIds());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> removeFavoriteMovie(@AuthenticationPrincipal CustomPrincipal principal,
                                                    @PathVariable("movieId") Long movieId) {
        favoritesService.removeFavoriteMovie(principal.getUserId(), movieId);
        return ResponseEntity.noContent().build();
    }
}
