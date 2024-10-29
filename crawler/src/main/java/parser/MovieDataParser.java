package parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.recommender.common.model.movie.Movie;
import com.movie.recommender.common.model.movie.MovieAllData;
import com.movie.recommender.common.model.movie.MovieCredits;
import com.movie.recommender.common.model.movie.MovieKeywords;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieDataParser {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<MovieAllData> parseMovieData(String filePath) throws IOException {
        JsonNode rootNode = objectMapper.readTree(new File(filePath));
        List<MovieAllData> movieAllDataList = new ArrayList<>();

        for (JsonNode entryNode : rootNode) {
            Movie movie = objectMapper.treeToValue(entryNode.path("detail").get(0), Movie.class);
            MovieCredits movieCredits = objectMapper.treeToValue(entryNode.path("credit").get(0), MovieCredits.class);
            MovieKeywords movieKeywords = objectMapper.treeToValue(entryNode.path("keyword").get(0), MovieKeywords.class);

            MovieAllData movieAllData = new MovieAllData();
            movieAllData.setMovie(movie);
            movieAllData.setMovieCredits(movieCredits);
            movieAllData.setMovieKeywords(movieKeywords);

            movieAllDataList.add(movieAllData);
        }

        return movieAllDataList;
    }
}