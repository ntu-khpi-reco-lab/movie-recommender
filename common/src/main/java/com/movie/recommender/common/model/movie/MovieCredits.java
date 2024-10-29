package com.movie.recommender.common.model.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class MovieCredits {
    private int id;
    private List<CastMember> cast;
    private List<CrewMember> crew;

    @Data
    public static class CastMember {
        private int id;
        private boolean adult;
        private String character;

        @JsonProperty("cast_id")
        private int castId;

        @JsonProperty("credit_id")
        private String creditId;

        private String name;

        @JsonProperty("original_name")
        private String originalName;

        private int gender;
        private double popularity;

        @JsonProperty("profile_path")
        private String profilePath;

        private int order;

        @JsonProperty("known_for_department")
        private String knownForDepartment;
    }

    @Data
    public static class CrewMember {
        private int id;
        private boolean adult;
        private String department;
        private String job;

        @JsonProperty("credit_id")
        private String creditId;

        private String name;

        @JsonProperty("original_name")
        private String originalName;

        private int gender;
        private double popularity;

        @JsonProperty("profile_path")
        private String profilePath;

        @JsonProperty("known_for_department")
        private String knownForDepartment;
    }
}