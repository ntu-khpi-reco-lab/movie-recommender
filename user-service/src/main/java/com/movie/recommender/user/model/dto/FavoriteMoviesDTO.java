package com.movie.recommender.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteMoviesDTO {
    private List<Long> movieIds;
}
