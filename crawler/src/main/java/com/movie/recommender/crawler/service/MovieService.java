package com.movie.recommender.crawler.service;

import com.movie.recommender.common.client.LocationServiceClient;

import com.movie.recommender.common.model.location.CountryWithCitiesDTO;
import com.movie.recommender.common.model.movie.MovieCredits;
import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.common.model.movie.MovieKeywords;
import com.movie.recommender.common.model.movie.NowPlayingMoviesByCountry;
import com.movie.recommender.common.model.showtime.Showtime;
import com.movie.recommender.common.model.showtime.ShowtimesByCity;
import com.movie.recommender.crawler.client.SerpApiClient;
import com.movie.recommender.crawler.client.TmdbApiClient;
import com.movie.recommender.crawler.model.showtime.MovieShowtimesResponse;
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

    public MovieService(LocationServiceClient locationServiceClient, MongoDBService mongoDBService) {
        this.locationServiceClient = locationServiceClient;
        this.tmdbApiClient = new TmdbApiClient();
        this.serpApiClient = new SerpApiClient();
        this.mongoDBService = mongoDBService;
    }

    public void loadNowPlayingMovies() {
        Map<String, String> countryCodeMapping = Map.of(
                "Ukraine", "ua"
        );

        List<CountryWithCitiesDTO> countries = locationServiceClient.getAllCountriesAndCities();

        List<NowPlayingMoviesByCountry> nowPlayingMoviesList = new ArrayList<>();

        for (CountryWithCitiesDTO countryWithCities : countries) {
            String countryName = countryWithCities.getCountryName();
            String countryCode = countryCodeMapping.get(countryName);

            if (countryCode == null) {
                log.warn("Country code not found for country '{}'. Skipping...", countryName);
                continue;
            }

            log.info("Loading now playing movies for country '{}'", countryName);

            Optional<NowPlayingMoviesByCountry> nowPlayingMovie = tmdbApiClient.getNowPlayingMovies(countryCode);
            if (nowPlayingMovie.isPresent()) {
                nowPlayingMovie.get().setCountryCode(countryCode);
                mongoDBService.insertNowPlayingMovies(nowPlayingMovie.get());
                nowPlayingMoviesList.add(nowPlayingMovie.get());
                log.info("Now playing movies for country '{}' inserted into MongoDB.", countryName);
            } else {
                log.warn("No now playing movies found for country '{}'.", countryName);
            }
        }

        // Process movie details for all now playing movies
        processNowPlayingMoviesDetails(nowPlayingMoviesList);
    }


    public void processNowPlayingMoviesDetails(List<NowPlayingMoviesByCountry> nowPlayingMoviesList) {
        List<MovieDetails> movieDetailsList = new ArrayList<>();

        for (NowPlayingMoviesByCountry nowPlayingMovies : nowPlayingMoviesList) {
            for (NowPlayingMoviesByCountry.MovieIdentifier movie : nowPlayingMovies.getResults()) {
                // Fetch movie details using the new method
                Optional<MovieDetails> movieDetailsOpt = fetchMovieDetails(movie.getId());
                movieDetailsOpt.ifPresent(movieDetailsList::add);
            }
        }

        // Insert all collected movie details into MongoDB
        mongoDBService.insertMovies(movieDetailsList);
        log.info("All movie details inserted into the 'movies' collection.");
    }

    private Optional<MovieDetails> fetchMovieDetails(Long movieId) {
        // Check if the movie already exists in MongoDB
        if (mongoDBService.existsMovieById(movieId)) {
            log.info("Movie ID: {} already exists in MongoDB. Skipping...", movieId);
            return Optional.empty();
        }

        log.info("Processing Movie ID: {}", movieId);

        // Load movie details
        Optional<MovieDetails> movieDetailsOpt = tmdbApiClient.getMovieDetails(movieId.toString());
        if (movieDetailsOpt.isEmpty()) {
            log.warn("No details found for movie ID: {}", movieId);
            return Optional.empty();
        }

        MovieDetails movieDetails = movieDetailsOpt.get();

        // Load movie credits
        Optional<MovieCredits> creditsOpt = tmdbApiClient.getMovieCredits(movieId.toString());
        creditsOpt.ifPresent(credits -> {
            movieDetails.setCast(credits.getCast());
            movieDetails.setCrew(credits.getCrew());
        });

        // Load movie keywords
        Optional<MovieKeywords> keywordsOpt = tmdbApiClient.getMovieKeywords(movieId.toString());
        keywordsOpt.ifPresent(keywords -> movieDetails.setKeywords(keywords.getKeywords()));

        return Optional.of(movieDetails);
    }


    public void loadShowtimes() {
        Map<String, String> countryCodeMapping = Map.of(
                "Ukraine", "ua"
        );

        List<NowPlayingMoviesByCountry> nowPlayingMovies = mongoDBService.getNowPlayingMovies();
        List<CountryWithCitiesDTO> countries = locationServiceClient.getAllCountriesAndCities();
        int apiCallCount = 0;

        List<ShowtimesByCity> allShowtimes = new ArrayList<>();

        // Iterate through each country and city
        for (CountryWithCitiesDTO countryWithCities : countries) {
            String countryName = countryWithCities.getCountryName();
            String countryCode = countryCodeMapping.get(countryName);

            if (countryCode == null) {
                log.warn("Country code not found for country '{}'. Skipping...", countryName);
                continue;
            }

            log.info("Loading showtimes for country '{}'", countryName);

            for (String cityName : countryWithCities.getCities()) {
                ShowtimesByCity showtimesByCity = new ShowtimesByCity();
                showtimesByCity.setCountryCode(countryCode);
                showtimesByCity.setCityName(cityName);

                List<ShowtimesByCity.MovieIdentifier> movieList = new ArrayList<>();

                // Iterate through now playing movies for the current country
                for (NowPlayingMoviesByCountry nowPlayingMovie : nowPlayingMovies) {
                    if (!nowPlayingMovie.getCountryCode().equals(countryCode)) {
                        continue;
                    }

                    for (NowPlayingMoviesByCountry.MovieIdentifier movie : nowPlayingMovie.getResults()) {
                        // Stop processing if the API call limit of 3 is reached

                        ShowtimesByCity.MovieIdentifier showtimeMovie = new ShowtimesByCity.MovieIdentifier();
                        showtimeMovie.setId(movie.getId());
                        showtimeMovie.setTitle(movie.getTitle());

                        // Fetch showtimes from the API
                        Optional<MovieShowtimesResponse> showtimesResponse = serpApiClient.getMovieShowtimes(movie.getTitle(), cityName, "en");

                        if (showtimesResponse.isPresent()) {
                            showtimeMovie.setShowtimes(showtimesResponse.get().getShowtimes());
                            movieList.add(showtimeMovie);
                            apiCallCount++;
                        }

                        // Check API call limit again
                        if (apiCallCount >= 3) {
                            log.info("Reached the limit of 3 API calls. Saving collected data and exiting.");
                            showtimesByCity.setMovies(movieList);
                            allShowtimes.add(showtimesByCity);
                            for (ShowtimesByCity showtimes : allShowtimes) {
                                mongoDBService.insertShowtimes(showtimes);
                            }
                            return;
                        }
                    }
                }

                // Add the current city's showtimes to the batch
                showtimesByCity.setMovies(movieList);
                allShowtimes.add(showtimesByCity);
            }
        }
    }

}
