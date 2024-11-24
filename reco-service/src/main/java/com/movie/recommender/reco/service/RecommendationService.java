package com.movie.recommender.reco.service;

import com.movie.recommender.common.client.FavoritesClient;
import com.movie.recommender.common.client.LocationServiceClient;
import com.movie.recommender.common.client.MovieRecoClient;
import com.movie.recommender.common.model.location.LocationDTO;
import com.movie.recommender.common.model.movie.NowPlayingMoviesByCountry;
import com.movie.recommender.common.model.reco.PredictRequest;
import com.movie.recommender.common.model.reco.PredictResponse;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecommendationService {

    private final LocationServiceClient locationClient;
    private final FavoritesClient favoritesClient;
    private final MongoDBService mongoDBService;
    private final MovieRecoClient movieRecoClient;
    private final String COUNTRY_CODE = "ua";

    public RecommendationService(
            LocationServiceClient locationClient,
            FavoritesClient favoritesClient,
            MongoDBService mongoDBService,
            MovieRecoClient movieRecoClient
    ) {
        this.locationClient = locationClient;
        this.favoritesClient = favoritesClient;
        this.mongoDBService = mongoDBService;
        this.movieRecoClient = movieRecoClient;
    }



    public Map<String, Object> getRecommendations(Long userId) {
        log.info("Fetching location for user ID: {}", userId);
        LocationDTO location = locationClient.getLocationByUserId(userId);

        log.info("Fetching favorite movies for user ID: {}", userId);
        Set<Long> favoriteMovies = favoritesClient.getFavoriteMovies(userId);

        log.info("Fetching now playing movies for country code: {}", COUNTRY_CODE);
        List<NowPlayingMoviesByCountry> nowPlayingMovies = mongoDBService.getNowPlayingMoviesByCountry(COUNTRY_CODE);

        List<Long> nowPlayingMovieIds = nowPlayingMovies.stream()
                .flatMap(country -> country.getResults().stream())
                .map(NowPlayingMoviesByCountry.MovieIdentifier::getId)
                .collect(Collectors.toList());
        log.info("Now playing movie IDs: {}", nowPlayingMovieIds);

        PredictRequest predictRequest = new PredictRequest();
        predictRequest.setLikedMovieIds(new ArrayList<>(favoriteMovies));
        predictRequest.setMovieIds(nowPlayingMovieIds);
        log.info("Sending predict request: {}", predictRequest);

        PredictResponse predictResponse = movieRecoClient.getRecommendations(predictRequest);
        log.info("Received predict response: {}", predictResponse);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("location", location);
        response.put("favorites", favoriteMovies);
        response.put("recommendations", predictResponse);

        return response;
    }
}