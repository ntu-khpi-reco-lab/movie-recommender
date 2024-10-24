CREATE TABLE IF NOT EXISTS locations (
    id BIGSERIAL PRIMARY KEY,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    user_id BIGINT UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_user_id ON locations(user_id);