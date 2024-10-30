package dataimport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.movie.recommender.common.model.movie.MovieCredits.CastMember;
import com.movie.recommender.common.model.movie.MovieCredits.CrewMember;
import com.movie.recommender.common.model.movie.MovieKeywords.Keyword;
import com.movie.recommender.common.model.movie.MovieDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieDataParser {
    private static final Logger logger = LoggerFactory.getLogger(MovieDataParser.class);
    private final ObjectMapper objectMapper;

    public MovieDataParser() {
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<MovieDetails> parseMovieData(String filePath) {
        List<MovieDetails> movieDetailsList = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            for (JsonNode entryNode : rootNode) {
                // Parse basic movie details
                MovieDetails movieDetails = objectMapper.treeToValue(entryNode.path("detail").get(0), MovieDetails.class);

                // Parse cast, crew, and keywords directly
                List<CastMember> cast = objectMapper.convertValue(entryNode.path("credit").get(0).path("cast"),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, CastMember.class));
                List<CrewMember> crew = objectMapper.convertValue(entryNode.path("credit").get(0).path("crew"),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, CrewMember.class));
                List<Keyword> keywords = objectMapper.convertValue(entryNode.path("keyword").get(0).path("keywords"),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Keyword.class));

                // Set cast, crew, and keywords in MovieDetails
                movieDetails.setCast(cast);
                movieDetails.setCrew(crew);
                movieDetails.setKeywords(keywords);

                movieDetailsList.add(movieDetails);
            }
        } catch (IOException e) {
            logger.error("Error reading movie data from file: {}", filePath, e);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while parsing movie data: {}", e.getMessage(), e);
        }

        return movieDetailsList;
    }
}