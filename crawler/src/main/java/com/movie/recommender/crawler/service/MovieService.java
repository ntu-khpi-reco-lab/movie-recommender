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
    int apiCallCount = 0;

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

            NowPlayingMoviesByCountry movies = fetchNowPlayingMoviesForCountry(country);
            if (apiCallCount < API_CALL_LIMIT) {
                log.info("Reached the limit of {} API calls. Saving collected data and exiting.", API_CALL_LIMIT);
                processCountryShowtimes(country, movies);
            }
        }
    }


    private NowPlayingMoviesByCountry fetchNowPlayingMoviesForCountry(CountryWithCitiesDTO country) {
        String countryName = country.getCountryName();
        String countryCode = countryCodeMapping.get(countryName);

        log.info("Loading now playing movies for country '{}'", countryName);

        Optional<NowPlayingMoviesByCountry> nowPlayingMovie = tmdbApiClient.getNowPlayingMovies(countryCode);
        if (nowPlayingMovie.isPresent()) {
            nowPlayingMovie.get().setCountryCode(countryCode);
            mongoDBService.insertNowPlayingMovies(nowPlayingMovie.get());
            log.info("Now playing movies for country '{}' inserted into MongoDB.", countryName);
        } else {
            log.warn("No now playing movies found for country '{}'.", countryName);
        }

        processNowPlayingMoviesDetails(nowPlayingMovie);
        return nowPlayingMovie.get();
    }


    public void processNowPlayingMoviesDetails(Optional<NowPlayingMoviesByCountry> nowPlayingMovie) {
        List<MovieDetails> movieDetailsList = new ArrayList<>();

        for (NowPlayingMoviesByCountry.MovieIdentifier movie : nowPlayingMovie.get().getResults()) {
            // Fetch movie details using the new method
            Optional<MovieDetails> movieDetailsOpt = fetchMovieDetails(movie.getId());
            movieDetailsOpt.ifPresent(movieDetailsList::add);
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

    private void processCountryShowtimes(CountryWithCitiesDTO country, NowPlayingMoviesByCountry nowPlayingMoviesByCountry) {
        List<ShowtimesByCity> allShowtimes = new ArrayList<>();

        String countryName = country.getCountryName();
        String countryCode = nowPlayingMoviesByCountry.getCountryCode();

        if (countryCode == null) {
            log.warn("Country code not found for country '{}'. Skipping...", countryName);
            return;
        }

        log.info("Loading showtimes for country '{}'", countryName);

        // Перебираем города страны
        for (String cityName : country.getCities()) {
            if (apiCallCount >= API_CALL_LIMIT) {
                log.info("Reached the limit of {} API calls. Exiting city loop.", API_CALL_LIMIT);
                break;
            }

            ShowtimesByCity showtimesByCity = new ShowtimesByCity();
            showtimesByCity.setCountryCode(countryCode);
            showtimesByCity.setCityName(cityName);

            // Получаем список сеансов для фильмов в текущем городе
            List<ShowtimesByCity.MovieShowtimes> movieList = processMovies(nowPlayingMoviesByCountry.getResults(), cityName);

            // Добавляем фильмы города в список
            showtimesByCity.setMovies(movieList);
            allShowtimes.add(showtimesByCity);
        }

        // Сохраняем все собранные данные в MongoDB
        if (!allShowtimes.isEmpty()) {
            log.info("Saving collected showtimes data for country '{}'.", countryName);
            saveShowtimes(allShowtimes);
        } else {
            log.warn("No showtimes data collected for country '{}'. Skipping save operation.", countryName);
        }
    }

    private List<ShowtimesByCity.MovieShowtimes> processMovies(List<NowPlayingMoviesByCountry.MovieIdentifier> movies, String cityName) {
        List<ShowtimesByCity.MovieShowtimes> movieList = new ArrayList<>();

        for (NowPlayingMoviesByCountry.MovieIdentifier movie : movies) {
            if (apiCallCount >= API_CALL_LIMIT) {
                log.info("Reached the limit of {} API calls. Exiting movie loop.", API_CALL_LIMIT);
                break;
            }

            // Получаем объект MovieShowtimes для текущего фильма
            ShowtimesByCity.MovieShowtimes showtimeMovie = fetchMovieShowtimes(movie, cityName);

            // Если расписание успешно загружено, добавляем в список
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


    // Метод для сохранения данных в MongoDB
    private void saveShowtimes(List<ShowtimesByCity> allShowtimes) {
        for (ShowtimesByCity showtimes : allShowtimes) {
            mongoDBService.insertShowtimes(showtimes);
        }
    }
}
