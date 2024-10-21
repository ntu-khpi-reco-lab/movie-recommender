package com.movie.recommender.crawler.client;

import com.movie.recommender.common.model.Movie;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TmdbApiClientTest {
    private TmdbApiClient tmdbApiClient;

    @Before
    public void setUp() {
        tmdbApiClient = new TmdbApiClient();
    }

    @Test
    @DisplayName("Test fetching movie details and validating the title")
    public void testGetMovieDetails() {
        // Fetch movie details for movie ID 671 (Harry Potter and the Philosopher's Stone)
        Optional<Movie> movieDetails = tmdbApiClient.getMovieDetails("671");

        // Assert that the movie details are not null
        assertTrue("Movie details should not be null", movieDetails.isPresent());

        // Assert that the movie title is "Harry Potter and the Philosopher's Stone"
        assertEquals("Movie title should be 'Harry Potter and the Philosopher's Stone'",
                "Harry Potter and the Philosopher's Stone",
                movieDetails.get().getTitle());
    }
}
