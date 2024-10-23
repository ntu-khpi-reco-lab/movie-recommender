package com.movie.recommender.common.model;

import lombok.Data;

import java.util.List;

@Data
public class Movie {
    private boolean adult;
    private String backdropPath;
    private Collection belongsToCollection;
    private int budget;
    private List<Genre> genres;
    private String homepage;
    private int id;
    private String imdbId;
    private List<String> originCountry;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private double popularity;
    private String posterPath;
    private List<ProductionCompany> productionCompanies;
    private List<ProductionCountry> productionCountries;
    private String releaseDate;
    private long revenue;
    private int runtime;
    private List<SpokenLanguage> spokenLanguages;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private double voteAverage;
    private int voteCount;


    @Data
    public static class MovieList {
        private List<Movie> results;
        private Dates dates;
        private int page;
        private int totalResults;
        private int totalPages;
    }


    @Data
    public static class Dates{
        private String maximum;
        private String minimum;
    }

    @Data
    public static class MovieCredits{
        private int id;
        private List<CastMember> cast;

    }

    @Data
    public static class MovieKeywords{
        private int id;
        private List<Keyword> keywords;

    }

    @Data
    public static class Keyword{
        private int id;
        private String name;
    }

    @Data
    public static class CastMember{
        private boolean adult;
        private int gender;
        private int id;
        private String knownForDepartment;
        private String name;
        private String originalName;
        private double popularity;
        private String profilePath;
        private int castId;
        private String character;
        private String creditId;
        private int order;
    }


    @Data
    public static class Collection {
        private int id;
        private String name;
        private String posterPath;
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
        private String logoPath;
        private String name;
        private String originCountry;
    }

    @Data
    public static class ProductionCountry {
        private String iso3166_1;
        private String name;
    }

    @Data
    public static class SpokenLanguage {
        private String englishName;
        private String iso639_1;
        private String name;
    }
}
