package com.movie.recommender.crawler.service;

import com.movie.recommender.common.client.LocationServiceClient;
import com.movie.recommender.common.model.location.CountryWithCitiesDTO;
import com.movie.recommender.common.model.movie.Movie;
import com.movie.recommender.common.model.movie.MovieList;
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

    public MovieService(LocationServiceClient locationServiceClient, TmdbApiClient tmdbApiClient) {
        this.locationServiceClient = locationServiceClient;
        this.tmdbApiClient = tmdbApiClient;
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



    public void FetchShowtimesByUserLocations(){
        List<String> locations =  parseLocations();
        List<String> moviesList = new ArrayList<>();
        Optional<MovieList> movies = tmdbApiClient.getNowPlayingMovies();

        if (movies.isPresent()) {
            movies.get().getResults().forEach(movie -> moviesList.add(movie.getTitle()));

            for (Movie movie : movies.get().getResults()) {
                // Add logic here to handle each movie and location
            }
        } else {
            log.info("No movies found");
        }
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
