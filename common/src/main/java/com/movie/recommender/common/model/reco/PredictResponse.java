package com.movie.recommender.common.model.reco;

import lombok.Data;

import java.util.List;

@Data
public class PredictResponse {
    private List<Prediction> predictions;
}
