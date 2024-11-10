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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            mongoDBService.insertNowPlayingMovies(nowPlayingMovie.get());
            log.info("Now playing movies for country '{}' loaded and inserted into MongoDB.", country);
        } else {
            log.warn("No now playing movies found for country '{}'.", country);
        }
        loadNowPlayingMoviesToMovieDetail(nowPlayingMovie);

    }

    public void loadNowPlayingMoviesToMovieDetail(Optional<NowPlayingMoviesByCountry> nowPlayingMovie) {
        if (nowPlayingMovie.isEmpty()) {
            log.warn("No now playing movies found.");
            return;
        }

        List<MovieDetails> movieDetailsList = new ArrayList<>();

        for (NowPlayingMoviesByCountry.MoviesIdResult movie : nowPlayingMovie.get().getResults()) {
            Long movieId = movie.getId();
            log.info("Processing Movie ID: {}", movieId);

            // Загружаем детали фильма
            Optional<MovieDetails> movieDetailsOpt = tmdbApiClient.getMovieDetails(movieId.toString());
            if (movieDetailsOpt.isEmpty()) {
                log.warn("No details found for movie ID: {}", movieId);
                continue;
            }

            MovieDetails movieDetails = movieDetailsOpt.get();

            // Загружаем актерский состав
            Optional<MovieCredits> creditsOpt = tmdbApiClient.getMovieCredits(movieId.toString());
            creditsOpt.ifPresent(credits -> {
                movieDetails.setCast(credits.getCast());
                movieDetails.setCrew(credits.getCrew());
            });

            // Загружаем ключевые слова
            Optional<MovieKeywords> keywordsOpt = tmdbApiClient.getMovieKeywords(movieId.toString());
            keywordsOpt.ifPresent(keywords -> movieDetails.setKeywords(keywords.getKeywords()));

            // Добавляем детали фильма в список
            movieDetailsList.add(movieDetails);
            log.info("Details for movie ID: {} added to the list.", movieId);
        }

        // Вставляем все собранные данные в MongoDB
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
