package com.movie.recommender.user.service;

import com.movie.recommender.user.exception.MovieNotFoundException;
import com.movie.recommender.user.exception.UserAlreadyExistsException;
import com.movie.recommender.user.security.JwtUtil;
import com.movie.recommender.user.exception.UserNotFoundException;
import com.movie.recommender.user.model.dto.UserCreateDTO;
import com.movie.recommender.user.model.dto.UserDTO;
import com.movie.recommender.user.model.entity.User;
import com.movie.recommender.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserDTO registerUser(UserCreateDTO userCreateDTO) {
        log.info("Attempting to register user with username: {}", userCreateDTO.getUsername());

        if (userRepository.findByUsername(userCreateDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = userCreateDTO.toEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);

        log.info("User registered successfully with ID: {}", user.getId());
        return UserDTO.toDTO(user);
    }

    public String login(String username, String password) {
        log.info("Attempting login for username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            log.info("Login successful for username: {}", username);
            return jwtUtil.generateToken(username);
        } else {
            log.warn("Login failed: Invalid credentials for username {}", username);
            throw new IllegalArgumentException("Invalid credentials");
        }
    }

    public Set<Long> getFavoriteMovies(Long userId) {
        log.info("Attempting to get favorite movies for user with id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return user.getFavoriteMovies();
    }

    public void addFavoriteMovies(Long userId, List<Long> movieIds) {
        log.info("Attempting to add favorite movies for user with id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.getFavoriteMovies().addAll(movieIds);
        userRepository.save(user);
    }

    public void removeFavoriteMovies(Long userId, List<Long> movieIds) {
        log.info("Attempting to remove favorite movies for user with id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.getFavoriteMovies().removeAll(movieIds);
        userRepository.save(user);
    }

    public void removeFavoriteMovie(Long userId, Long movieId) {
        log.info("Attempting to remove favorite movie for user with id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        boolean removed = user.getFavoriteMovies().remove(movieId);
        if (!removed) {
            throw new MovieNotFoundException("Movie not found in favorites, id: " + movieId);
        }

        userRepository.save(user);
    }

}
