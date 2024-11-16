package com.movie.recommender.user.service;

import com.movie.recommender.user.exception.UserAlreadyExistsException;
import com.movie.recommender.user.security.JwtUtil;
import com.movie.recommender.user.exception.UserNotFoundException;
import com.movie.recommender.user.model.dto.UserCreateDTO;
import com.movie.recommender.user.model.dto.UserDTO;
import com.movie.recommender.user.model.entity.User;
import com.movie.recommender.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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
}
