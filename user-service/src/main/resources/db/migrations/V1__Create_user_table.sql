CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username DOUBLE PRECISION NOT NULL,
    password DOUBLE PRECISION NOT NULL,
    location_id BIGINT UNIQUE NOT NULL
);

CREATE INDEX idx_location_id ON users(location_id);