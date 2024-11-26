package com.movie.recommender.reco.service;

import com.movie.recommender.common.client.FavoritesClient;
import com.movie.recommender.common.client.LocationServiceClient;
import com.movie.recommender.common.client.MovieRecoClient;
import com.movie.recommender.common.model.location.LocationDTO;
import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.common.model.movie.NowPlayingMoviesByCountry;
import com.movie.recommender.common.model.reco.MovieWithShowtime;
import com.movie.recommender.common.model.reco.PredictRequest;
import com.movie.recommender.common.model.reco.PredictResponse;
import com.movie.recommender.common.model.reco.Prediction;
import com.movie.recommender.common.model.showtime.Showtime;
import com.movie.recommender.common.model.showtime.ShowtimesByCity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    @Value("${reco.threshold}")
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

    public List<MovieWithShowtime> getRecommendations(Long userId) {
        log.info("Starting recommendation process for user ID: {}", userId);

        LocationDTO location = fetchUserLocation(userId);
        Set<Long> favoriteMovies = fetchUserFavoriteMovies(userId);
        List<Long> nowPlayingMovieIds = fetchNowPlayingMovieIds();

        PredictRequest predictRequest = createPredictRequest(favoriteMovies, nowPlayingMovieIds);
        PredictResponse predictResponse = fetchPredictions(predictRequest);

        // Filter predictions
        List<Long> filteredMovieIds = filterPredictions(predictResponse, scoreThreshold);

        // Fetch movie details for the filtered predictions
        return fetchMoviesWithShowtimes(filteredMovieIds,location);
    }

    private LocationDTO fetchUserLocation(Long userId) {
        log.info("Fetching location for user ID: {}", userId);
        return locationClient.getLocationByUserId(userId);
    }

    private Set<Long> fetchUserFavoriteMovies(Long userId) {
        log.info("Fetching favorite movies for user ID: {}", userId);
        return favoritesClient.getFavoriteMovies(userId);
    }

    private List<Long> fetchNowPlayingMovieIds() {
        log.info("Fetching now playing movies for country code: {}", COUNTRY_CODE);
        List<NowPlayingMoviesByCountry> nowPlayingMovies = mongoDBService.getNowPlayingMoviesByCountry(COUNTRY_CODE);
        return nowPlayingMovies.stream()
                .flatMap(country -> country.getResults().stream())
                .map(NowPlayingMoviesByCountry.MovieIdentifier::getId)
                .collect(Collectors.toList());
    }

    private PredictRequest createPredictRequest(Set<Long> favoriteMovies, List<Long> nowPlayingMovieIds) {
        PredictRequest predictRequest = new PredictRequest();
        predictRequest.setLikedMovieIds(new ArrayList<>(favoriteMovies));
        predictRequest.setMovieIds(nowPlayingMovieIds);
        log.info("Created predict request: {}", predictRequest);
        return predictRequest;
    }

    private PredictResponse fetchPredictions(PredictRequest predictRequest) {
        log.info("Sending predict request: {}", predictRequest);
        PredictResponse predictResponse = movieRecoClient.getRecommendations(predictRequest);
        log.info("Received predict response: {}", predictResponse);
        return predictResponse;
    }

    private List<Long> filterPredictions(PredictResponse predictResponse, double threshold) {
        log.info("Filtering predictions with threshold: {}", threshold);
        return predictResponse.getPredictions().stream()
                .filter(prediction -> prediction.getScore() >= threshold)
                .map(Prediction::getMovieId)
                .collect(Collectors.toList());
    }

    private List<MovieWithShowtime> fetchMoviesWithShowtimes(List<Long> movieIds, LocationDTO location) {
        log.info("Fetching movies with showtimes for IDs: {}", movieIds);

        List<MovieWithShowtime> moviesWithShowtimes = new ArrayList<>();
        for (Long movieId : movieIds) {
            MovieDetails movieDetails = mongoDBService.getMovieDetailsById(movieId);

            // Fetch showtimes using the user's location
            List<Showtime> showtimes = mongoDBService.getMovieShowtimesByCity(
                    COUNTRY_CODE,
                    location.getCityName(),
                    movieId
            );

            if (movieDetails != null) {
                moviesWithShowtimes.add(new MovieWithShowtime(movieDetails, showtimes));
            }
        }

        log.info("Fetched {} movies with showtimes", moviesWithShowtimes.size());
        return moviesWithShowtimes;
    }
}
