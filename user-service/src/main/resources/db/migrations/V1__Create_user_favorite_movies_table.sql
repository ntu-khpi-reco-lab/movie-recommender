CREATE TABLE IF NOT EXISTS user_favorite_movies (
    user_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, movie_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
