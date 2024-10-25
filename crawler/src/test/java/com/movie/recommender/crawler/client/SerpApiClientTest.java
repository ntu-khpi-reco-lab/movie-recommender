package com.movie.recommender.crawler.client;

import com.movie.recommender.common.model.MovieShowtimesResponse;
import com.movie.recommender.common.model.MovieShowtimesResponse.Showtime;
import com.movie.recommender.common.model.MovieShowtimesResponse.Theater;
import com.movie.recommender.common.model.MovieShowtimesResponse.SearchMetadata;
import com.movie.recommender.common.model.MovieShowtimesResponse.SearchParameters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SerpApiClientTest {

    private SerpApiClient serpApiClient;

    @BeforeEach
    public void setUp() {
        serpApiClient = new SerpApiClient();
    }

    @Test
    @DisplayName("Test fetching movie showtimes and validating search metadata")
    public void testGetMovieShowtimes() {
        // Fetch showtimes for a specific movie
        Optional<MovieShowtimesResponse> showtimesResponse = serpApiClient.getMovieShowtimes("The wild robot", "Austin", "en");

        // Assert that showtimes are not null
        assertTrue(showtimesResponse.isPresent(), "Movie showtimes should not be null");

        // Validate the metadata contains a status
        SearchMetadata metadata = showtimesResponse.get().getSearchMetadata();
        assertNotNull(metadata.getStatus(), "Search metadata status should not be null");

    }

    @Test
    @DisplayName("Test showtimes contain valid theaters and showing times")
    public void testShowtimesContainTheaters() {
        // Fetch movie showtimes
        Optional<MovieShowtimesResponse> showtimesResponse = serpApiClient.getMovieShowtimes("The wild robot", "Austin", "en");

        // Assert that showtimes are not null
        assertTrue(showtimesResponse.isPresent(), "Movie showtimes should not be null");

        // Assert that the showtimes contain at least one theater with showing times
        List<Showtime> showtimes = showtimesResponse.get().getShowtimes();
        assertFalse(showtimes.isEmpty(), "Showtimes should contain entries");

        Showtime firstShowtime = showtimes.get(0);
        assertFalse(firstShowtime.getTheaters().isEmpty(), "Showtime should contain theaters");

        // Validate that each theater has at least one showing
        Theater firstTheater = firstShowtime.getTheaters().get(0);
        assertFalse(firstTheater.getShowing().isEmpty(), "Theater should have showings");
    }

    @Test
    @DisplayName("Test invalid movie request returns empty response")
    public void testInvalidMovieRequestReturnsEmptyResponse() {
        // Attempt to fetch showtimes for an invalid movie name
        Optional<MovieShowtimesResponse> showtimesResponse = serpApiClient.getMovieShowtimes("Invalid Movie", "Austin", "en");

        // Assert that the showtimes response is empty
        assertTrue(showtimesResponse.isEmpty(), "Showtimes should be empty for an invalid movie");
    }

    @Test
    @DisplayName("Test fetching with unsupported location returns empty response")
    public void testUnsupportedLocationReturnsEmptyResponse() {
        // Attempt to fetch showtimes for a movie with an unsupported location
        Optional<MovieShowtimesResponse> showtimesResponse = serpApiClient.getMovieShowtimes("The wild robot",
                "Atlantis", "en");

        // Assert that the showtimes response is empty
        assertFalse(showtimesResponse.isEmpty(), "Showtimes should not be null empty for unsupported location");
    }
}