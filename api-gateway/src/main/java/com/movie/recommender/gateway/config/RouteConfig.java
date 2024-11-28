package com.movie.recommender.gateway.config;

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;

@Configuration
public class RouteConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("location-service", r -> r
                        .path("/api/v1/locations/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("location-service-cb")
                                        .setFallbackUri("forward:/locations")))
                        .uri("http://location-service:9001"))  // Ensure the port matches the Location Service
                .route("user-service", r -> r
                        .path("/api/v1/users/**", "/api/v1/favorites/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("user-service-cb")
                                        .setFallbackUri("forward:/users")))
                        .uri("http://user-service:9002"))  // Ensure the port matches the User Service
                .route("search-service", r -> r
                        .path("/api/v1/search/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("search-service-cb")
                                        .setFallbackUri("forward:/search")))
                        .uri("http://search-service:9005")) // Ensure the port matches the Search Service
                .route("reco-service", r -> r
                        .path("/api/v1/recommend/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("reco-service-cb")
                                        .setFallbackUri("forward:/recommend")))
                        .uri("http://reco-service:9006")) // Ensure the port matches the Reco Service
                .build();
    }
}
