CREATE TABLE IF NOT EXISTS countries(
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS cities (
  id SERIAL PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  country_id INT REFERENCES countries(id) ON DELETE CASCADE
);

ALTER TABLE locations
ADD COLUMN city_id BIGINT REFERENCES cities(id);

CREATE INDEX idx_city_id ON locations(city_id);