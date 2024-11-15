package com.movie.recommender.user.controller;

import com.movie.recommender.user.model.dto.*;
import com.movie.recommender.user.model.entity.User;
import com.movie.recommender.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        UserDTO userDTO = userService.registerUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        String token = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/{userId}/favorites")
    public ResponseEntity<Set<Long>> getFavoriteMovies(@PathVariable("userId") Long userId) {
        Set<Long> favoriteMovies = userService.getFavoriteMovies(userId);
        return ResponseEntity.ok(favoriteMovies);
    }

    @PostMapping("/{userId}/favorites")
    public ResponseEntity<Void> addFavoriteMovies(@PathVariable("userId") Long userId, @RequestBody AddFavoriteMoviesDTO favoriteMoviesDTO) {
        userService.addFavoriteMovies(userId, favoriteMoviesDTO.getMovieIds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{userId}/favorites")
    public ResponseEntity<Void> removeFavoriteMovies(@PathVariable("userId") Long userId, @RequestBody AddFavoriteMoviesDTO favoriteMoviesDTO) {
        userService.removeFavoriteMovies(userId, favoriteMoviesDTO.getMovieIds());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/favorites/{movieId}")
    public ResponseEntity<Void> removeFavoriteMovie(@PathVariable("userId") Long userId, @PathVariable Long movieId) {
        userService.removeFavoriteMovie(userId, movieId);
        return ResponseEntity.noContent().build();
    }

}

