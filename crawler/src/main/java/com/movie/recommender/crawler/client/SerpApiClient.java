package com.movie.recommender.crawler.client;

import showtime.MovieShowtimesResponse;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

@Slf4j
public class SerpApiClient extends ApiClient {

    public SerpApiClient() {
        super("SERP_API_KEY");
    }

    public Optional<MovieShowtimesResponse > getMovieShowtimes(String movieName, String location, String language) {
        String url = UrlBuilder.movieShowtimesUrl(movieName, location, language, this.apiKey );
        return callApi(url, MovieShowtimesResponse.class);
    }
}