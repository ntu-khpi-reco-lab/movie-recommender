package com.movie.recommender.crawler.client;

import showtime.MovieShowtimesResponse;
import showtime.SearchMetadata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;


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
}