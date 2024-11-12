package com.movie.recommender.crawler.service;

import com.movie.recommender.common.client.LocationServiceClient;

import com.movie.recommender.common.model.location.CountryWithCitiesDTO;
import com.movie.recommender.common.model.movie.MovieCredits;
import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.common.model.movie.MovieKeywords;
import com.movie.recommender.common.model.movie.NowPlayingMoviesByCountry;
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
    private final Map<String, String> countryCodeMapping = Map.of("Ukraine", "ua");
    private static final int API_CALL_LIMIT = 3;

    public MovieService(LocationServiceClient locationServiceClient, MongoDBService mongoDBService) {
        this.locationServiceClient = locationServiceClient;
        this.tmdbApiClient = new TmdbApiClient();
        this.serpApiClient = new SerpApiClient();
        this.mongoDBService = mongoDBService;
    }

    public void processCountryMovies() {
        List<CountryWithCitiesDTO> countries = locationServiceClient.getAllCountriesAndCities();
        for (CountryWithCitiesDTO country : countries) {
            String countryName = country.getCountryName();
            String countryCode = countryCodeMapping.get(countryName);

            if (countryCode == null) {
                log.warn("Country code not found for country '{}'. Skipping...", countryName);
                continue;
            }

            log.info("Processing country '{}'", countryName);

            NowPlayingMoviesByCountry movies = fetchNowPlayingMoviesForCountry(countryCode);
            processCountryShowtimes(country, movies);

        }
    }


    private NowPlayingMoviesByCountry fetchNowPlayingMoviesForCountry(String countryCode) {


        log.info("Loading now playing movies for country '{}'", countryCode);

        Optional<NowPlayingMoviesByCountry> nowPlayingMovie = tmdbApiClient.getNowPlayingMovies(countryCode);
        if (nowPlayingMovie.isPresent()) {
            nowPlayingMovie.get().setCountryCode(countryCode);
            mongoDBService.insertNowPlayingMovies(nowPlayingMovie.get());
            processNowPlayingMoviesDetails(nowPlayingMovie.get());
            log.info("Now playing movies for country '{}' inserted into MongoDB.", countryCode);
        } else {
            log.warn("No now playing movies found for country '{}'.", countryCode);
        }

        return nowPlayingMovie.get();
    }


    public void processNowPlayingMoviesDetails(NowPlayingMoviesByCountry nowPlayingMovie) {
        List<MovieDetails> movieDetailsList = new ArrayList<>();

        for (NowPlayingMoviesByCountry.MovieIdentifier movie : nowPlayingMovie.getResults()) {
            Optional<MovieDetails> movieDetailsOpt = fetchMovieDetails(movie.getId());
            movieDetailsOpt.ifPresent(movieDetailsList::add);
        }

        mongoDBService.insertMovies(movieDetailsList);
        log.info("All movie details inserted into the 'movies' collection.");
    }

    private Optional<MovieDetails> fetchMovieDetails(Long movieId) {
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

        Optional<MovieCredits> creditsOpt = tmdbApiClient.getMovieCredits(movieId.toString());
        creditsOpt.ifPresent(credits -> {
            movieDetails.setCast(credits.getCast());
            movieDetails.setCrew(credits.getCrew());
        });

        Optional<MovieKeywords> keywordsOpt = tmdbApiClient.getMovieKeywords(movieId.toString());
        keywordsOpt.ifPresent(keywords -> movieDetails.setKeywords(keywords.getKeywords()));

        return Optional.of(movieDetails);
    }

    private void processCountryShowtimes(CountryWithCitiesDTO country, NowPlayingMoviesByCountry nowPlayingMoviesByCountry) {
        String countryCode = nowPlayingMoviesByCountry.getCountryCode();
        String countryName = country.getCountryName();

        log.info("Loading showtimes for country '{}'", countryCode);

        for (String cityName : country.getCities()) {
            ShowtimesByCity showtimesByCity = new ShowtimesByCity();
            showtimesByCity.setCountryCode(countryCode);
            showtimesByCity.setCityName(cityName);

            List<ShowtimesByCity.MovieShowtimes> movieList = processMovies(nowPlayingMoviesByCountry.getResults(), cityName,countryName);

            showtimesByCity.setMovies(movieList);
            mongoDBService.insertShowtimes(showtimesByCity);
        }

    }

    private List<ShowtimesByCity.MovieShowtimes> processMovies(List<NowPlayingMoviesByCountry.MovieIdentifier> movies, String cityName, String country) {
        List<ShowtimesByCity.MovieShowtimes> movieList = new ArrayList<>();
        int apiCallCount = 0;
        for (NowPlayingMoviesByCountry.MovieIdentifier movie : movies) {
            if (apiCallCount >= API_CALL_LIMIT) {
                log.info("Reached the limit of {} API calls. Exiting movie loop.", API_CALL_LIMIT);
                break;
            }
            String cityWithCountryName =country + "," + cityName ;
            ShowtimesByCity.MovieShowtimes showtimeMovie = fetchMovieShowtimes(movie, cityWithCountryName);

            if (showtimeMovie != null) {
                movieList.add(showtimeMovie);
                apiCallCount++;
            }
        }
        return movieList;
    }

    private ShowtimesByCity.MovieShowtimes fetchMovieShowtimes(NowPlayingMoviesByCountry.MovieIdentifier movie, String cityName) {
        ShowtimesByCity.MovieShowtimes showtimeMovie = new ShowtimesByCity.MovieShowtimes();
        showtimeMovie.setId(movie.getId());
        showtimeMovie.setTitle(movie.getTitle());

        Optional<MovieShowtimesResponse> showtimesResponse = serpApiClient.getMovieShowtimes(movie.getTitle(), cityName, "en");

        if (showtimesResponse.isPresent()) {
            showtimeMovie.setShowtimes(showtimesResponse.get().getShowtimes());
            return showtimeMovie;
        }

        return null;
    }
}
