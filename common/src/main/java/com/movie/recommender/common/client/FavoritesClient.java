package com.movie.recommender.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Set;

@FeignClient(name = "user-service", url = "${service.user.service.url}")
public interface FavoritesClient {

    @GetMapping("/api/v1/favorites")
    Set<Long> getFavoriteMovies(@RequestHeader("Authorization") String token);
}