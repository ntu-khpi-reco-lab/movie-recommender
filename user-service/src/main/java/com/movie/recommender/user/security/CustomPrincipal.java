package com.movie.recommender.user.security;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomPrincipal {
    private final Long userId;
    private final String username;

}