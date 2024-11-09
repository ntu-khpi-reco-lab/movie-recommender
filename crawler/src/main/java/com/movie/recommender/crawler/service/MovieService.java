package com.movie.recommender.crawler.service;

import com.movie.recommender.common.client.LocationServiceClient;
import com.movie.recommender.common.model.location.CountryWithCitiesDTO;
import com.movie.recommender.common.model.movie.Movie;
import com.movie.recommender.common.model.movie.MovieList;
import com.movie.recommender.crawler.client.SerpApiClient;
import com.movie.recommender.crawler.client.TmdbApiClient;
import com.movie.recommender.crawler.model.showtime.MovieShowtimesResponse;
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

    public MovieService(LocationServiceClient locationServiceClient) {
        this.locationServiceClient = locationServiceClient;
        this.tmdbApiClient = new TmdbApiClient();
        this.serpApiClient = new SerpApiClient();
    }

    public List<String> parseLocations() {
        List<CountryWithCitiesDTO> locations = locationServiceClient.getAllCountriesAndCities();
        List<String> locationStrings = new ArrayList<>();

        // Iterate through each country and its cities
        locations.forEach(countryWithCitiesDTO -> {
            String countryName = countryWithCitiesDTO.getCountryName();
            countryWithCitiesDTO.getCities().forEach(city -> {
                // Create a string in the format "City, Country"
                String locationString = city + " " + countryName;
                locationStrings.add(locationString);
            });
        });

        log.info("Generated location strings: {}", locationStrings);
        return locationStrings;
    }



    public List<Optional<MovieShowtimesResponse>> FetchShowtimesByUserLocations() {
        List<String> locations = parseLocations();
        List<String> moviesList = new ArrayList<>();
        Optional<MovieList> movies = tmdbApiClient.getNowPlayingMovies();
        List<Optional<MovieShowtimesResponse>> result = new ArrayList<>();  // Initialize the list here
        String language = "en";

        if (movies.isPresent()) {
            movies.get().getResults().forEach(movie -> moviesList.add(movie.getTitle()));
            log.info(String.valueOf(moviesList));

            for (String loc : locations) {
                for (String movieTitle : moviesList) {
                    result.add(serpApiClient.getMovieShowtimes(movieTitle, loc, language));
                }
            }
        } else {
            log.info("No movies found");
        }
        log.info(result.toString());
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
