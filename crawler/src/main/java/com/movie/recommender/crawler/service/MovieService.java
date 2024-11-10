package com.movie.recommender.crawler.service;

import com.movie.recommender.common.client.LocationServiceClient;

import com.movie.recommender.common.model.movie.MovieCredits;
import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.common.model.movie.MovieKeywords;
import com.movie.recommender.common.model.movie.NowPlayingMoviesByCountry;
import com.movie.recommender.crawler.client.SerpApiClient;
import com.movie.recommender.crawler.client.TmdbApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class MovieService {
    private final LocationServiceClient locationServiceClient;
    private final TmdbApiClient tmdbApiClient;
    private final SerpApiClient serpApiClient;
    private final MongoDBService mongoDBService;
    private final String DEFAULT_COUNTRY = "ua";

    public MovieService(LocationServiceClient locationServiceClient, MongoDBService mongoDBService) {
        this.locationServiceClient = locationServiceClient;
        this.tmdbApiClient = new TmdbApiClient();
        this.serpApiClient = new SerpApiClient();
        this.mongoDBService = mongoDBService;
    }

    public void loadNowPlayingMovies() {
        String country = DEFAULT_COUNTRY;
        Optional<NowPlayingMoviesByCountry> nowPlayingMovie = tmdbApiClient.getNowPlayingMovies(country);
        if (nowPlayingMovie.isPresent()) {
            nowPlayingMovie.get().setCountry(country);
            mongoDBService.insertNowPlayingMovies(nowPlayingMovie.get());
        } else {
            log.warn("No now playing movies found for country '{}'.", country);
        }


    }


    public void loadMovieDetailsFromNowPlaying() {
        List<NowPlayingMoviesByCountry> nowPlayingMoviesList = mongoDBService.getNowPlayingMovies();

        Set<Long> movieIds = new HashSet<>();
        for (NowPlayingMoviesByCountry nowPlayingMovies : nowPlayingMoviesList) {
            for (NowPlayingMoviesByCountry.MoviesIdResult movie : nowPlayingMovies.getResults()) {
                movieIds.add(movie.getId());
            }
        }

        List<MovieDetails> movieDetailsList = new ArrayList<>();
        for (Long movieId : movieIds) {
            log.info("Processing Movie ID: {}", movieId);

            Optional<MovieDetails> movieDetailsOpt = tmdbApiClient.getMovieDetails(movieId.toString());
            if (movieDetailsOpt.isEmpty()) {
                log.warn("No details found for movie ID: {}", movieId);
                continue;
            }

            MovieDetails movieDetails = movieDetailsOpt.get();

            Optional<MovieCredits> creditsOpt = tmdbApiClient.getMovieCredits(movieId.toString());
            creditsOpt.ifPresent(credits -> {
                movieDetails.setCast(credits.getCast());
                movieDetails.setCrew(credits.getCrew());
            });

            Optional<MovieKeywords> keywordsOpt = tmdbApiClient.getMovieKeywords(movieId.toString());
            keywordsOpt.ifPresent(keywords -> movieDetails.setKeywords(keywords.getKeywords()));

            movieDetailsList.add(movieDetails);
            log.info("Details for movie ID: {} added to the list.", movieId);
        }

        mongoDBService.insertMovies(movieDetailsList);
    }




    // For testing purposes. Verify that the country with cities is retrieved.
    // Should be removed later.
    public void getCountryWithCities() {
        log.info("Getting country with cities...");
        locationServiceClient.getAllCountriesAndCities().forEach(countryWithCitiesDTO -> {
            log.info("Country: {}", countryWithCitiesDTO.getCountryName());
            countryWithCitiesDTO.getCities().forEach(city -> log.info("City: {}", city));
        });
    }



}
