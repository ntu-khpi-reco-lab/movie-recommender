package com.movie.recommender.reco.service;

import com.movie.recommender.common.client.FavoritesClient;
import com.movie.recommender.common.client.LocationServiceClient;
import com.movie.recommender.common.client.MovieRecoClient;
import com.movie.recommender.common.model.location.LocationDTO;
import com.movie.recommender.common.model.movie.NowPlayingMoviesByCountry;
import com.movie.recommender.common.model.reco.PredictRequest;
import com.movie.recommender.common.model.reco.PredictResponse;
import com.movie.recommender.common.model.reco.Prediction;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${reco.threshold}")  // Read the threshold value from the configuration file
    private double scoreThreshold;

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

    public List<Prediction> getRecommendations(Long userId) {
        log.info("Starting recommendation process for user ID: {}", userId);

        LocationDTO location = fetchUserLocation(userId);
        Set<Long> favoriteMovies = fetchUserFavoriteMovies(userId);
        List<Long> nowPlayingMovieIds = fetchNowPlayingMovieIds();

        PredictRequest predictRequest = createPredictRequest(favoriteMovies, nowPlayingMovieIds);
        PredictResponse predictResponse = fetchPredictions(predictRequest);

        return filterPredictions(predictResponse, scoreThreshold);
    }

    // Fetch user's location
    private LocationDTO fetchUserLocation(Long userId) {
        log.info("Fetching location for user ID: {}", userId);
        return locationClient.getLocationByUserId(userId);
    }

    // Fetch user's favorite movies
    private Set<Long> fetchUserFavoriteMovies(Long userId) {
        log.info("Fetching favorite movies for user ID: {}", userId);
        return favoritesClient.getFavoriteMovies(userId);
    }

    // Fetch IDs of now-playing movies
    private List<Long> fetchNowPlayingMovieIds() {
        log.info("Fetching now playing movies for country code: {}", COUNTRY_CODE);
        List<NowPlayingMoviesByCountry> nowPlayingMovies = mongoDBService.getNowPlayingMoviesByCountry(COUNTRY_CODE);
        return nowPlayingMovies.stream()
                .flatMap(country -> country.getResults().stream())
                .map(NowPlayingMoviesByCountry.MovieIdentifier::getId)
                .collect(Collectors.toList());
    }

    // Create a PredictRequest object
    private PredictRequest createPredictRequest(Set<Long> favoriteMovies, List<Long> nowPlayingMovieIds) {
        PredictRequest predictRequest = new PredictRequest();
        predictRequest.setLikedMovieIds(new ArrayList<>(favoriteMovies));
        predictRequest.setMovieIds(nowPlayingMovieIds);
        log.info("Created predict request: {}", predictRequest);
        return predictRequest;
    }

    // Fetch predictions from the recommendation service
    private PredictResponse fetchPredictions(PredictRequest predictRequest) {
        log.info("Sending predict request: {}", predictRequest);
        PredictResponse predictResponse = movieRecoClient.getRecommendations(predictRequest);
        log.info("Received predict response: {}", predictResponse);
        return predictResponse;
    }

    // Filter predictions based on the score threshold
    private List<Prediction> filterPredictions(PredictResponse predictResponse, double threshold) {
        log.info("Filtering predictions with threshold: {}", threshold);
        List<Prediction> filteredPredictions = predictResponse.getPredictions().stream()
                .filter(prediction -> prediction.getScore() >= threshold)
                .collect(Collectors.toList());
        log.info("Filtered predictions: {}", filteredPredictions);
        return filteredPredictions;
    }
}