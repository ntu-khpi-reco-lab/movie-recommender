package com.movie.recommender.crawler.service;

import com.movie.recommender.common.client.LocationServiceClient;
import com.movie.recommender.common.client.MovieRecoClient;
import com.movie.recommender.common.model.location.CountryWithCitiesDTO;
import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.common.model.movie.NowPlayingMoviesByCountry;
import com.movie.recommender.common.model.showtime.ShowtimesByCity;
import com.movie.recommender.crawler.client.SerpApiClient;
import com.movie.recommender.crawler.client.TmdbApiClient;
import com.movie.recommender.crawler.model.showtime.MovieShowtimesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class MovieService {
    private final LocationServiceClient locationServiceClient;
    private final TmdbApiClient tmdbApiClient;
    private final SerpApiClient serpApiClient;
    private final MongoDBService mongoDBService;
    private final MovieRecoClient movieRecoClient;

    private static final int API_CALL_LIMIT = 3;

    public MovieService(LocationServiceClient locationServiceClient,
                        MongoDBService mongoDBService,
                        MovieRecoClient movieRecoClient) {
        this.locationServiceClient = locationServiceClient;
        this.movieRecoClient = movieRecoClient;
        this.tmdbApiClient = new TmdbApiClient();
        this.serpApiClient = new SerpApiClient();
        this.mongoDBService = mongoDBService;
    }

    @Async
    public void processCountryMovies() {
        List<CountryWithCitiesDTO> countries = locationServiceClient.getAllCountriesAndCities();
        for (CountryWithCitiesDTO country : countries) {
            String countryCode = country.getCountryCode();

            if (countryCode == null) {
                log.warn("Country code not found for country '{}'. Skipping...", country.getCountryName());
                continue;
            }

            log.info("Processing country '{}'", country.getCountryName());

            NowPlayingMoviesByCountry movies = fetchNowPlayingMoviesForCountry(countryCode,country.getCountryName());
            if (movies != null) {
                processCountryShowtimes(country, countryCode, movies);
            }
        }
        // Retrain the recommendation model after adding new movies
        movieRecoClient.retrain();
    }

    private NowPlayingMoviesByCountry fetchNowPlayingMoviesForCountry(String countryCode,String countryName) {
        log.info("Loading now playing movies for country '{}'", countryCode);

        Optional<NowPlayingMoviesByCountry> nowPlayingMovieOpt = tmdbApiClient.getNowPlayingMovies(countryCode);
        if (nowPlayingMovieOpt.isPresent()) {
            NowPlayingMoviesByCountry nowPlayingMovie = nowPlayingMovieOpt.get();
            nowPlayingMovie.setCountryCode(countryCode);
            nowPlayingMovie.setCountryName(countryName);
            mongoDBService.insertNowPlayingMovies(nowPlayingMovie);
            processNowPlayingMoviesDetails(nowPlayingMovie);
            log.info("Now playing movies for country '{}' inserted into MongoDB.", countryCode);
            return nowPlayingMovie;
        } else {
            log.warn("No now playing movies found for country '{}'.", countryCode);
            return null;
        }
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

        Optional<MovieDetails> movieDetailsOpt = tmdbApiClient.getMovieDetails(movieId.toString());
        if (movieDetailsOpt.isEmpty()) {
            log.warn("No details found for movie ID: {}", movieId);
            return Optional.empty();
        }

        MovieDetails movieDetails = movieDetailsOpt.get();

        tmdbApiClient.getMovieCredits(movieId.toString()).ifPresent(credits -> {
            movieDetails.setCast(credits.getCast());
            movieDetails.setCrew(credits.getCrew());
        });

        tmdbApiClient.getMovieKeywords(movieId.toString()).ifPresent(keywords -> movieDetails.setKeywords(keywords.getKeywords()));

        return Optional.of(movieDetails);
    }

    private void processCountryShowtimes(CountryWithCitiesDTO country, String countryCode, NowPlayingMoviesByCountry nowPlayingMovies) {
        log.info("Loading showtimes for country '{}'", countryCode);

        for (String cityName : country.getCities()) {
            ShowtimesByCity showtimesByCity = new ShowtimesByCity();
            showtimesByCity.setCountryCode(country.getCountryCode());
            showtimesByCity.setCountryName(country.getCountryName());
            showtimesByCity.setCityName(cityName);

            List<ShowtimesByCity.MovieShowtimes> movieList = processMovies(
                    nowPlayingMovies.getResults(),
                    cityName,
                    country.getCountryName()
            );

            showtimesByCity.setMovies(movieList);
            mongoDBService.insertShowtimes(showtimesByCity);
        }
    }

    private List<ShowtimesByCity.MovieShowtimes> processMovies(
            List<NowPlayingMoviesByCountry.MovieIdentifier> movies,
            String cityName,
            String country
    ) {
        List<ShowtimesByCity.MovieShowtimes> movieList = new ArrayList<>();
        int apiCallCount = 0;

        for (NowPlayingMoviesByCountry.MovieIdentifier movie : movies) {
            if (apiCallCount >= API_CALL_LIMIT) {
                log.info("Reached the limit of {} API calls. Exiting movie loop.", API_CALL_LIMIT);
                break;
            }

            String location = country + ", " + cityName;
            ShowtimesByCity.MovieShowtimes showtimeMovie = fetchMovieShowtimes(movie, location);

            if (showtimeMovie != null) {
                movieList.add(showtimeMovie);
                apiCallCount++;
            }
        }
        return movieList;
    }

    private ShowtimesByCity.MovieShowtimes fetchMovieShowtimes(NowPlayingMoviesByCountry.MovieIdentifier movie, String location) {
        ShowtimesByCity.MovieShowtimes showtimeMovie = new ShowtimesByCity.MovieShowtimes();
        showtimeMovie.setId(movie.getId());
        showtimeMovie.setTitle(movie.getTitle());

        Optional<MovieShowtimesResponse> showtimesResponse = serpApiClient.getMovieShowtimes(movie.getTitle(), location, "en");

        if (showtimesResponse.isPresent()) {
            showtimeMovie.setShowtimes(showtimesResponse.get().getShowtimes());
            return showtimeMovie;
        }

        return null;
    }

    public ShowtimesByCity getShowtimesByCity(String countryName, String cityName) {
        try {
            return mongoDBService.getShowtimesByCity(countryName, cityName);
        }
        catch (Exception e) {
            log.error("Error fetching showtimes for location [country: {}, city: {}]: {}", countryName, cityName, e.getMessage(), e);
            throw e;
        }
    }
}
