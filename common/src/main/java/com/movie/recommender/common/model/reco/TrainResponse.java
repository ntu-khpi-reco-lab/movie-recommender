package com.movie.recommender.common.model.reco;

import lombok.Data;

@Data
public class TrainResponse {
    private String message;
    private String modelPath;
}
