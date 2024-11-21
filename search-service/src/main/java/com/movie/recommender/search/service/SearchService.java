package com.movie.recommender.search.service;

import com.movie.recommender.common.model.movie.MovieDetails;
import com.movie.recommender.search.model.SearchFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SearchService {
    final private MongoTemplate mongoTemplate;

    public SearchService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<MovieDetails> search(SearchFilter filter) {
        Query query = new Query();

        if (filter.getMovieTitle() != null) {
            filter.validateMovieTitle();
            query.addCriteria(Criteria.where("title").regex(filter.getMovieTitle(), "i"));
            log.info("Filtering by movie title: {}", filter.getMovieTitle());
        }

        if (filter.getGenres() != null && !filter.getGenres().isEmpty()) {
            filter.validateGenres();
            query.addCriteria(Criteria.where("genres.name").all(filter.getGenres()));
            log.info("Filtering by genres: {}", filter.getGenres());
        }

        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            filter.validateDate();
            query.addCriteria(Criteria.where("releaseDate")
                    .gte(filter.getStartDate())
                    .lte(filter.getEndDate()));
            log.info("Filtering by release date range: {} - {}", filter.getStartDate(), filter.getEndDate());
        }

        if (filter.getRatingStart() != null && filter.getRatingEnd() != null) {
            filter.validateRating();
            query.addCriteria(Criteria.where("rating")
                    .gte(filter.getRatingStart())
                    .lte(filter.getRatingEnd()));
            log.info("Filtering by rating range: {} - {}", filter.getRatingStart(), filter.getRatingEnd());
        }

        if (filter.getIsAdult() != null && filter.getIsAdult()) {
            query.addCriteria(Criteria.where("adult").is(true));
            log.info("Filtering by adult content: true");
        }
        else if (filter.getIsAdult() != null && !filter.getIsAdult()){
            query.addCriteria(Criteria.where("adult").is(false));
            log.info("Filtering by adult content: false");
        }

        if (filter.getCastNames() != null && !filter.getCastNames().isEmpty()) {
            filter.validateCastNames();
            query.addCriteria(Criteria.where("cast.name").all(filter.getCastNames()));
            log.info("Filtering by cast names: {}", filter.getCastNames());
        }

        if (filter.getCrewNames() != null && !filter.getCrewNames().isEmpty()) {
            filter.validateCrewNames();
            query.addCriteria(Criteria.where("crew.name").all(filter.getCrewNames()));
            log.info("Filtering by crew names: {}", filter.getCrewNames());
        }

        if (filter.getKeywords() != null && !filter.getKeywords().isEmpty()) {
            query.addCriteria(Criteria.where("keywords.name").all(filter.getKeywords()));
            log.info("Filtering by keywords: {}", filter.getKeywords());
        }

        List<MovieDetails> result = mongoTemplate.find(query, MovieDetails.class);
        log.info("Query executed, found {} movies", result.size());
        return result;
    }
}
