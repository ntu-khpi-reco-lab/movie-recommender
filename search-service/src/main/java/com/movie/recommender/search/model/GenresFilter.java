package com.movie.recommender.search.model;

import java.util.List;

public interface GenresFilter {
    public final List<String> DEFAULT_GENRES = List.of(
            "Action",
            "Adventure",
            "Animation",
            "Comedy",
            "Crime",
            "Documentary",
            "Drama",
            "Family",
            "Fantasy",
            "History",
            "Horror",
            "Music",
            "Mystery",
            "Romance",
            "Science Fiction",
            "TV Movie",
            "Thriller",
            "War",
            "Western"

    );
    public void validateGenres();
}
