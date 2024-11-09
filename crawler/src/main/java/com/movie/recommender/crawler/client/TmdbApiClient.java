package com.movie.recommender.crawler.client;

import com.movie.recommender.lib.http.auth.AuthProvider;
import com.movie.recommender.lib.http.auth.BearerAuthProvider;
import com.movie.recommender.common.model.movie.Movie;
import com.movie.recommender.common.model.movie.MovieList;
import com.movie.recommender.common.model.movie.MovieCredits;
import com.movie.recommender.common.model.movie.MovieKeywords;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Component
@Slf4j
public class TmdbApiClient extends ApiClient {

    @Value("${tmdb.api.key}")
    private String apiKey;

    public TmdbApiClient() {
        super();
    }

    public Optional<Movie> getMovieDetails(String movieId) {
        String url = UrlBuilder.movieDetailsUrl(movieId);
        return callApi(url, Movie.class);
    }

    public Optional<MovieList> getNowPlayingMovies() {
        String url = UrlBuilder.nowPlayingMoviesUrl();
        return callApi(url, MovieList.class);
    }

    public Optional<MovieCredits> getMovieCredits(String movieId) {
        String url = UrlBuilder.movieCreditsUrl(movieId);
        return callApi(url, MovieCredits.class);
    }

    public Optional<MovieKeywords> getMovieKeywords(String movieId) {
        String url = UrlBuilder.movieKeywordsUrl(movieId);
        return callApi(url, MovieKeywords.class);
    }

    @Override
    protected AuthProvider createAuthProvider() {
        String authToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3ODg1YzczNjY0ZjNmOWQzZTJlNmE0MTk2Mzk4OThhYiIsIm5iZiI6MTczMTE2Mzg1OS42Mzg5MDUzLCJzdWIiOiI2NzAyZjhmOWM5YTEwZDQ2ZWE3ZDY1MjQiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.pJ_Agld4jedmMIDcaS1TEYIGs4Cew2RgyLvm-8f5vi8";
        return new BearerAuthProvider(authToken);
    }
}
