package com.movie.recommender.crawler.dataimport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.movie.recommender.common.model.movie.MovieCredits.CastMember;
import com.movie.recommender.common.model.movie.MovieCredits.CrewMember;
import com.movie.recommender.common.model.movie.MovieKeywords.Keyword;
import com.movie.recommender.common.model.movie.MovieDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MovieDataLoader {
    private final ObjectMapper objectMapper;

    public MovieDataLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<MovieDetails> parseMovieData(String filePath) {
        List<MovieDetails> movieDetailsList = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(new File(filePath));
            for (JsonNode entryNode : rootNode) {
                MovieDetails movieDetails = parseMovieEntry(entryNode);
                if (movieDetails != null) {
                    log.debug("Parsed movie: {}", movieDetails.getTitle());
                    movieDetailsList.add(movieDetails);
                }
            }
        } catch (IOException e) {
            log.error("Error reading movie data from file: {}", filePath, e);
        } catch (Exception e) {
            log.error("Unexpected error occurred while parsing movie data: {}", e.getMessage(), e);
        }
        return movieDetailsList;
    }

    private MovieDetails parseMovieEntry(JsonNode entryNode) {
        try {
            // Parse basic movie details
            MovieDetails movieDetails = objectMapper.treeToValue(entryNode.path("detail").get(0), MovieDetails.class);

            // Parse and set cast, crew, and keywords
            movieDetails.setCast(parseList(entryNode.path("credit").get(0).path("cast"), CastMember.class));
            movieDetails.setCrew(parseList(entryNode.path("credit").get(0).path("crew"), CrewMember.class));
            movieDetails.setKeywords(parseList(entryNode.path("keyword").get(0).path("keywords"), Keyword.class));

            return movieDetails;
        } catch (Exception e) {
            log.error("Error parsing movie entry: {}", e.getMessage(), e);
            return null;
        }
    }

    private <T> List<T> parseList(JsonNode node, Class<T> clazz) {
        return objectMapper.convertValue(node, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}