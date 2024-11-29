package com.movie.recommender.common.model.reco;

import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.common.model.showtime.Showtime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieWithShowtime {
    private MovieDetails movieDetails;
    private List<Showtime> showtimes;
}