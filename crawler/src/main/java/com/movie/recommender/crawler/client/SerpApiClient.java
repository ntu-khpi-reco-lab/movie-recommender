package com.movie.recommender.crawler.client;

import com.movie.recommender.lib.http.auth.ApiKeyAuthProvider;
import com.movie.recommender.lib.http.auth.AuthProvider;
import com.movie.recommender.crawler.model.showtime.MovieShowtimesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class SerpApiClient extends ApiClient {
    public SerpApiClient() {
        super();
    }

    public Optional<MovieShowtimesResponse > getMovieShowtimes(String movieName, String location, String language) {
        String url = UrlBuilder.movieShowtimesUrl(movieName, location, language);
        return callApi(url, MovieShowtimesResponse.class);
    }

    @Override
    protected AuthProvider createAuthProvider() {
        String apiKey = "9dc14c3ae6901ba9fbc7e0257594425557b53a2399fda39f89ec67c074df9a56";
        return new ApiKeyAuthProvider(apiKey);
    }
}