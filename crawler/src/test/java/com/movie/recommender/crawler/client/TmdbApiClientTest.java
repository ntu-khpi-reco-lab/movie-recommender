package com.movie.recommender.crawler.client;

import com.movie.recommender.common.model.Movie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TmdbApiClientTest {
    private TmdbApiClient tmdbApiClient;

    @BeforeEach
    public void setUp() {
        tmdbApiClient = new TmdbApiClient();
    }

    @Test
    @DisplayName("Test fetching movie details and validating the title")
    public void testGetMovieDetails() {
        // Fetch movie details for movie ID 671 (Harry Potter and the Philosopher's Stone)
        Optional<Movie> movieDetails = tmdbApiClient.getMovieDetails("671");

        // Assert that the movie details are not null
        assertTrue(movieDetails.isPresent(), "Movie details should not be null");

        // Assert that the movie title is "Harry Potter and the Philosopher's Stone"
        assertEquals("Harry Potter and the Philosopher's Stone", movieDetails.get().getTitle(),
                "Movie title should be 'Harry Potter and the Philosopher's Stone'");
    }
}
