package com.movie.recommender.reco.service;

import com.movie.recommender.common.model.movie.NowPlayingMoviesByCountry;
import com.movie.recommender.location.model.dto.LocationDTO;
import com.movie.recommender.reco.client.FavoritesClient;
import com.movie.recommender.reco.client.LocationClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

@Service
public class RecommendationService {

    private final LocationClient locationClient;
    private final FavoritesClient favoritesClient;
    private final MongoDBService mongoDBService;
    private final String COUNTRY_CODE = "ua";

    public RecommendationService(
            LocationClient locationClient,
            FavoritesClient favoritesClient,MongoDBService mongoDBService
    ) {
        this.locationClient = locationClient;
        this.favoritesClient = favoritesClient;
        this.mongoDBService = mongoDBService;

    }

    // Основной метод для получения рекомендаций
    public Map<String, Object> getRecommendations(Long userId) {
        LocationDTO location = locationClient.getLocationByUserId(userId);

        Set<Long> favoriteMovies = favoritesClient.getFavoriteMovies(userId);

        List<NowPlayingMoviesByCountry> nowPlayingMovies = mongoDBService.getNowPlayingMoviesByCountry(COUNTRY_CODE);

        // Формируем ответ
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("favorites", favoriteMovies);
        response.put("nowPlaying", nowPlayingMovies);

        return response;
    }

}