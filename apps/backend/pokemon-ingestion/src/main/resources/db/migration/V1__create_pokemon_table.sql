CREATE TABLE pokemon (
    pokeapi_id INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    base_experience INTEGER,
    height INTEGER NOT NULL,
    weight INTEGER NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT TRUE
);
