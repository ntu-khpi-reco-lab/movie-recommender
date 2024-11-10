package com.movie.recommender.crawler.service;

import com.movie.recommender.common.client.LocationServiceClient;
import com.movie.recommender.common.model.location.CountryWithCitiesDTO;
import com.movie.recommender.common.model.movie.Movie;
import com.movie.recommender.common.model.movie.MovieList;
import com.movie.recommender.common.model.movie.NowPlayingMovieDetail;
import com.movie.recommender.common.model.movie.NowPlayingMoviesByCountry;
import com.movie.recommender.crawler.client.SerpApiClient;
import com.movie.recommender.crawler.client.TmdbApiClient;
import com.movie.recommender.crawler.model.showtime.MovieShowtimesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        // Получаем список фильмов типа 'Movie' из TMDB API
        String country = "UA";
        Optional<MovieList> movieListOptional = tmdbApiClient.getNowPlayingMovies(country);

        if (movieListOptional.isPresent()) {
            MovieList movieList = movieListOptional.get();

            // Извлекаем даты из ответа API
            String startDate = movieList.getDates().getMinimum();
            String endDate = movieList.getDates().getMaximum();

            // Проверяем, есть ли фильмы в ответе
            if (movieList.getResults() != null && !movieList.getResults().isEmpty()) {
                List<Movie> movies = movieList.getResults();

                // Преобразуем список 'Movie' в 'NowPlayingMovieDetail'
                List<NowPlayingMovieDetail> movieDetailsList = movies.stream().map(movie -> {
                    NowPlayingMovieDetail movieDetail = new NowPlayingMovieDetail();
                    movieDetail.setId((long) movie.getId());
                    movieDetail.setTitle(movie.getTitle());
                    movieDetail.setReleaseDate(movie.getReleaseDate());
                    movieDetail.setOverview(movie.getOverview());
                    movieDetail.setPosterPath(movie.getPosterPath());
                    movieDetail.setBackdropPath(movie.getBackdropPath());
                    movieDetail.setPopularity(movie.getPopularity());
                    movieDetail.setVoteAverage(movie.getVoteAverage());
                    movieDetail.setVoteCount(movie.getVoteCount());
                    movieDetail.setGenres(movie.getGenres());
                    return movieDetail;
                }).collect(Collectors.toList());

                // Создаем объект 'NowPlayingMoviesByCountry'
                NowPlayingMoviesByCountry nowPlayingMovies = new NowPlayingMoviesByCountry();
                nowPlayingMovies.setCountry(country);
                nowPlayingMovies.setStartDate(startDate);
                nowPlayingMovies.setEndDate(endDate);
                nowPlayingMovies.setMovies(movieDetailsList);

                // Сохраняем данные в MongoDB
                mongoDBService.insertNowPlayingMovies(nowPlayingMovies);
                log.info("Now playing movies loaded for country '{}'", country);
            } else {
                log.warn("No now playing movies found for country '{}'", country);
            }
        } else {
            log.warn("Failed to fetch now playing movies for country '{}'", country);
        }
    }


    public List<String> parseLocations() {
        List<CountryWithCitiesDTO> locations = locationServiceClient.getAllCountriesAndCities();
        List<String> locationStrings = new ArrayList<>();
        locations.forEach(countryWithCitiesDTO -> {
            String countryName = countryWithCitiesDTO.getCountryName();
            countryWithCitiesDTO.getCities().forEach(city -> locationStrings.add(city + " " + countryName));
        });
        return locationStrings;
    }

    public List<Optional<MovieShowtimesResponse>> fetchShowtimesByUserLocations() {
        List<String> locations = parseLocations();
        List<Optional<MovieShowtimesResponse>> result = new ArrayList<>();
        String language = "en";

        // Получаем список "NowPlayingMoviesByCountry"
        List<NowPlayingMoviesByCountry> nowPlayingMoviesList = mongoDBService.getNowPlayingMovies("UA");

        if (!nowPlayingMoviesList.isEmpty()) {
            // Извлекаем фильмы из каждого объекта NowPlayingMoviesByCountry
            for (NowPlayingMoviesByCountry nowPlaying : nowPlayingMoviesList) {
                List<NowPlayingMovieDetail> moviesList = nowPlaying.getMovies();

                for (String loc : locations) {
                    for (NowPlayingMovieDetail movie : moviesList) {
                        log.info("Fetching showtimes for movie '{}' in location '{}'", movie.getTitle(), loc);
                        result.add(serpApiClient.getMovieShowtimes(movie.getTitle(), loc, language));
                    }
                }
            }
        } else {
            log.warn("No 'NowPlaying' movies found for region 'UA'.");
        }
        return result;
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
