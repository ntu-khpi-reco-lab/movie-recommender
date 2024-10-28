package com.movie.recommender.location;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// https://geocode.xyz/{lat},{long}?geoit=json - API для отримання більш точної геолокації
@SpringBootApplication
public class LocationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LocationServiceApplication.class, args);
    }
}
