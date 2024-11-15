package com.movie.recommender.user.service;

import com.movie.recommender.user.exception.MovieNotFoundException;
import com.movie.recommender.user.exception.UserNotFoundException;
import com.movie.recommender.user.model.entity.User;
import com.movie.recommender.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class FavoritesService {
    private final UserRepository userRepository;

    @Autowired
    public FavoritesService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Set<Long> getFavoriteMovies(Long userId) {
        log.info("Attempting to get favorite movies for user with id: {}", userId);
        User user = findUserById(userId);
        return user.getFavoriteMovies();
    }

    @Transactional
    public void addFavoriteMovies(Long userId, List<Long> movieIds) {
        log.info("Attempting to add favorite movies for user with id: {}", userId);
        User user = findUserById(userId);

        user.getFavoriteMovies().addAll(movieIds);
        userRepository.save(user);
    }

    @Transactional
    public void removeFavoriteMovies(Long userId, List<Long> movieIds) {
        log.info("Attempting to remove favorite movies for user with id: {}", userId);
        User user = findUserById(userId);

        user.getFavoriteMovies().removeAll(movieIds);
        userRepository.save(user);
    }

    @Transactional
    public void removeFavoriteMovie(Long userId, Long movieId) {
        log.info("Attempting to remove favorite movie for user with id: {}", userId);
        User user = findUserById(userId);

        boolean removed = user.getFavoriteMovies().remove(movieId);
        if (!removed) {
            throw new MovieNotFoundException("Movie not found in favorites, id: " + movieId);
        }

        userRepository.save(user);
    }

    private User findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return user;
    }
}
