package com.movie.recommender.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Set;

@FeignClient(name = "user-service", url = "${service.user.service.url}")
public interface FavoritesClient {

    @GetMapping("/api/v1/favorites/{userId}")
    Set<Long> getFavoriteMovies(@PathVariable("userId") Long userId);
}