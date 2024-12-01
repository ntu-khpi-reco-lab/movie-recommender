package com.movie.recommender.location.security;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomPrincipal {
    private final Long userId;
    private final String username;

}