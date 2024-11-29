package com.movie.recommender.common.model.reco;

import lombok.Data;

import java.util.List;

@Data
public class PredictRequest {
    private List<Long> movieIds;
    private List<Long> likedMovieIds;
}
