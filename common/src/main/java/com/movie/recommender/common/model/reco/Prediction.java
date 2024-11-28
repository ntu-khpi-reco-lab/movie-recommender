package com.movie.recommender.common.model.reco;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Prediction {
    private Long movieId;
    private Double score;
}
