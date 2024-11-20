package com.movie.recommender.reco.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@FeignClient(name = "user-service", url = "http://localhost:9002/api/v1/favorites")
public interface FavoritesClient {

    @GetMapping("/{userId}")
    Set<Long> getFavoriteMovies(@PathVariable("userId") Long userId);
}