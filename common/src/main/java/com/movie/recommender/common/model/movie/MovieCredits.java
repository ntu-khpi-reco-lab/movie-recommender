package com.movie.recommender.common.model.movie;

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
        private int castId;
        private String creditId;
        private String name;
        private String originalName;
        private int gender;
        private double popularity;
        private String profilePath;
        private int order;
        private String knownForDepartment;
    }

    @Data
    public static class CrewMember {
        private int id;
        private boolean adult;
        private String department;
        private String job;
        private String creditId;
        private String name;
        private String originalName;
        private int gender;
        private double popularity;
        private String profilePath;
        private String knownForDepartment;
    }
}