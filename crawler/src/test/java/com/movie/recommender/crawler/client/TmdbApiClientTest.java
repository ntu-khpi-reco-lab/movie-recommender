package com.movie.recommender.crawler.client;

import com.movie.recommender.common.model.movie.Movie;
import com.movie.recommender.common.model.movie.MovieCredits;
import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.common.model.movie.MovieKeywords;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

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
        Optional<MovieDetails> movieDetails = tmdbApiClient.getMovieDetails("671");

        // Assert that the movie details are not null
        assertTrue(movieDetails.isPresent(), "Movie details should not be null");

        // Assert that the movie title is "Harry Potter and the Philosopher's Stone"
        assertEquals("Harry Potter and the Philosopher's Stone", movieDetails.get().getTitle(),
                "Movie title should be 'Harry Potter and the Philosopher's Stone'");
    }


    @Test
    @DisplayName("Test fetching movie credits")
    public void testGetMovieCredits() {
        // Fetch movie credits for movie ID 671
        Optional<MovieCredits> movieCredits = tmdbApiClient.getMovieCredits("671");

        // Assert that movie credits are not null
        assertTrue(movieCredits.isPresent(), "Movie credits should not be null");

        // Validate that a known cast member (e.g., Daniel Radcliffe) is present
        boolean isDanielRadcliffeInCast = movieCredits.get().getCast().stream()
                .anyMatch(castMember -> castMember.getName().equals("Daniel Radcliffe")
                        && castMember.getCharacter().equals("Harry Potter"));
        assertTrue(isDanielRadcliffeInCast, "Daniel Radcliffe should be in the cast and play Harry Potter");

        // Optionally, you can add more checks, e.g., for another cast member or more details
        boolean isEmmaWatsonInCast = movieCredits.get().getCast().stream()
                .anyMatch(castMember -> castMember.getName().equals("Emma Watson")
                        && castMember.getCharacter().equals("Hermione Granger"));
        assertTrue(isEmmaWatsonInCast, "Emma Watson should be in the cast and play Hermione Granger");
    }

    @Test
    @DisplayName("Test fetching movie keywords")
    public void testGetMovieKeywords() {
        // Fetch movie keywords for movie ID 671
        Optional<MovieKeywords> movieKeywords = tmdbApiClient.getMovieKeywords("671");

        // Assert that movie keywords are not null
        assertTrue(movieKeywords.isPresent(), "Movie keywords should not be null");

        // Assert that the keywords list contains keywords
        assertFalse(movieKeywords.get().getKeywords().isEmpty(), "Keywords should contain entries");
    }
}
