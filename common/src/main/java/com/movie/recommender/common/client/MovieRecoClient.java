package com.movie.recommender.common.client;

import com.movie.recommender.common.model.reco.PredictRequest;
import com.movie.recommender.common.model.reco.PredictResponse;
import com.movie.recommender.common.model.reco.Prediction;
import com.movie.recommender.common.model.reco.TrainResponse;
import com.movie.recommender.lib.http.HttpClient;
import com.movie.recommender.lib.http.auth.NoAuthProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class MovieRecoClient {
    private final String baseUrl;
    private final HttpClient httpClient;

    public MovieRecoClient() {
        this.baseUrl = getMovieRecoUrl();
        this.httpClient = new HttpClient(new NoAuthProvider());
    }

    public PredictResponse getRecommendations(PredictRequest request) {
        String url = baseUrl + "/recommend";
        log.info("Sending POST request to {}", url);

         PredictResponse predictResponse = httpClient.post(url, request, PredictResponse.class);

        log.info("Received response from MovieReco service: {}", predictResponse);
        return predictResponse;
    }

    public void retrain() {
        String url = baseUrl + "/train";
        log.info("Sending POST request to {}", url);

         httpClient.post(url, null, TrainResponse.class);

        log.info("Retraining request sent to MovieReco service");
    }

    private String getMovieRecoUrl() {
        String baseUrl = System.getenv("MOVIE_RECO_BASE_URL");
        if (baseUrl == null) {
            log.error("MOVIE_RECO_BASE_URL environment variable is not set");
            throw new IllegalStateException("MOVIE_RECO_BASE_URL environment variable is not set");
        }
        return baseUrl;
    }
}
