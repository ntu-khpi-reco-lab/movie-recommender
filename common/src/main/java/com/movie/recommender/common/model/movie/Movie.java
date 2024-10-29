package com.movie.recommender.common.model.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class Movie {
    private boolean adult;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("belongs_to_collection")
    private Collection belongsToCollection;

    private int budget;
    private List<Genre> genres;
    private String homepage;
    private int id;

    @JsonProperty("imdb_id")
    private String imdbId;

    @JsonProperty("origin_country")
    private List<String> originCountry;

    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("original_title")
    private String originalTitle;

    private String overview;
    private double popularity;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("production_companies")
    private List<ProductionCompany> productionCompanies;

    @JsonProperty("production_countries")
    private List<ProductionCountry> productionCountries;

    @JsonProperty("release_date")
    private String releaseDate;

    private long revenue;
    private int runtime;

    @JsonProperty("spoken_languages")
    private List<SpokenLanguage> spokenLanguages;

    private String status;
    private String tagline;
    private String title;
    private boolean video;

    @JsonProperty("vote_average")
    private double voteAverage;

    @JsonProperty("vote_count")
    private int voteCount;

    @Data
    public static class Collection {
        private int id;
        private String name;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonProperty("backdrop_path")
        private String backdropPath;
    }

    @Data
    public static class Genre {
        private int id;
        private String name;
    }

    @Data
    public static class ProductionCompany {
        private int id;

        @JsonProperty("logo_path")
        private String logoPath;

        private String name;

        @JsonProperty("origin_country")
        private String originCountry;
    }

    @Data
    public static class ProductionCountry {
        @JsonProperty("iso_3166_1")
        private String iso3166_1;
        private String name;
    }

    @Data
    public static class SpokenLanguage {
        @JsonProperty("english_name")
        private String englishName;

        @JsonProperty("iso_639_1")
        private String iso639_1;

        private String name;
    }
}