package com.movie.recommender.crawler.client;

import com.movie.recommender.lib.http.auth.ApiKeyAuthProvider;
import com.movie.recommender.lib.http.auth.AuthProvider;
import com.movie.recommender.crawler.model.showtime.MovieShowtimesResponse;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

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
        String apiKey = getApiKey("SERP_API_KEY");
        return new ApiKeyAuthProvider(apiKey);
    }
}