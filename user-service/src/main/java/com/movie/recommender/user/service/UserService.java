package com.movie.recommender.user.service;

import com.movie.recommender.user.exception.UserAlreadyExistsException;
import com.movie.recommender.user.model.dto.UserUpdateDTO;
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

    public UserDTO getUserProfile(Long userId) {
        log.info("Attempting to get profile info for user with id: {}", userId);
        User user = findUserById(userId);
        return UserDTO.toDTO(user);
    }

    public UserDTO updateUserProfile(Long userId, UserUpdateDTO userUpdateDTO) {
        log.info("Attempting to update profile info for user with id: {}", userId);

        User user = findUserById(userId);
        if (userUpdateDTO.getUsername() != null) user.setUsername(userUpdateDTO.getUsername());
        if (userUpdateDTO.getEmail() != null) user.setEmail(userUpdateDTO.getEmail());
        if (userUpdateDTO.getFirstName() != null) user.setFirstName(userUpdateDTO.getFirstName());
        if (userUpdateDTO.getLastName() != null) user.setLastName(userUpdateDTO.getLastName());
        if (userUpdateDTO.getPassword() != null) user.setPassword(userUpdateDTO.getPassword());

        userRepository.save(user);
        return UserDTO.toDTO(user);
    }

    public void deleteUserProfile(Long userId) {
        log.info("Attempting to delete profile for user with id: {}", userId);
        findUserById(userId);
        userRepository.deleteById(userId);
    }

    private User findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return user;
    }
}
