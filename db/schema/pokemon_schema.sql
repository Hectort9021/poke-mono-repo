-- PostgreSQL schema para modelar datos principales de PokeAPI.
-- Cubre entidades core: pokemon, especies, tipos, habilidades, stats, movimientos,
-- formas, cadenas evolutivas, generaciones y versiones.

BEGIN;

CREATE TABLE generation (
    id BIGSERIAL PRIMARY KEY,
    pokeapi_id INTEGER NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    region_name VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE version_group (
    id BIGSERIAL PRIMARY KEY,
    pokeapi_id INTEGER NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    generation_id BIGINT REFERENCES generation(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE pokemon_species (
    id BIGSERIAL PRIMARY KEY,
    pokeapi_id INTEGER NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE,
    base_happiness INTEGER,
    capture_rate INTEGER,
    gender_rate INTEGER,
    hatch_counter INTEGER,
    is_baby BOOLEAN NOT NULL DEFAULT FALSE,
    is_legendary BOOLEAN NOT NULL DEFAULT FALSE,
    is_mythical BOOLEAN NOT NULL DEFAULT FALSE,
    growth_rate_name VARCHAR(100),
    habitat_name VARCHAR(100),
    shape_name VARCHAR(100),
    color_name VARCHAR(100),
    generation_id BIGINT REFERENCES generation(id),
    evolution_chain_external_id INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE pokemon (
    id BIGSERIAL PRIMARY KEY,
    pokeapi_id INTEGER NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE,
    species_id BIGINT NOT NULL REFERENCES pokemon_species(id),
    base_experience INTEGER,
    height INTEGER NOT NULL,
    weight INTEGER NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT TRUE,
    order_index INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE pokemon_form (
    id BIGSERIAL PRIMARY KEY,
    pokeapi_id INTEGER NOT NULL UNIQUE,
    pokemon_id BIGINT NOT NULL REFERENCES pokemon(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    form_name VARCHAR(100),
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    is_battle_only BOOLEAN NOT NULL DEFAULT FALSE,
    is_mega BOOLEAN NOT NULL DEFAULT FALSE,
    order_index INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE pokemon_type (
    id BIGSERIAL PRIMARY KEY,
    pokeapi_id INTEGER NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL UNIQUE,
    generation_id BIGINT REFERENCES generation(id),
    move_damage_class_name VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE pokemon_ability (
    id BIGSERIAL PRIMARY KEY,
    pokeapi_id INTEGER NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE,
    is_main_series BOOLEAN NOT NULL DEFAULT TRUE,
    generation_id BIGINT REFERENCES generation(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE pokemon_stat (
    id BIGSERIAL PRIMARY KEY,
    pokeapi_id INTEGER NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE,
    game_index INTEGER,
    is_battle_only BOOLEAN NOT NULL DEFAULT FALSE,
    move_damage_class_name VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE pokemon_move (
    id BIGSERIAL PRIMARY KEY,
    pokeapi_id INTEGER NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE,
    accuracy INTEGER,
    power INTEGER,
    pp INTEGER,
    priority INTEGER,
    damage_class_name VARCHAR(50),
    move_type_id BIGINT REFERENCES pokemon_type(id),
    generation_id BIGINT REFERENCES generation(id),
    effect_chance INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE pokemon_game_version (
    id BIGSERIAL PRIMARY KEY,
    pokeapi_id INTEGER NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE,
    version_group_id BIGINT REFERENCES version_group(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE evolution_chain (
    id BIGSERIAL PRIMARY KEY,
    pokeapi_id INTEGER NOT NULL UNIQUE,
    baby_trigger_item_name VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE evolution_link (
    id BIGSERIAL PRIMARY KEY,
    chain_id BIGINT NOT NULL REFERENCES evolution_chain(id) ON DELETE CASCADE,
    from_species_id BIGINT REFERENCES pokemon_species(id),
    to_species_id BIGINT NOT NULL REFERENCES pokemon_species(id),
    min_level INTEGER,
    trigger_name VARCHAR(100),
    item_name VARCHAR(100),
    held_item_name VARCHAR(100),
    known_move_name VARCHAR(100),
    known_move_type_name VARCHAR(100),
    location_name VARCHAR(100),
    min_affection INTEGER,
    min_beauty INTEGER,
    min_happiness INTEGER,
    needs_overworld_rain BOOLEAN,
    party_species_name VARCHAR(100),
    party_type_name VARCHAR(100),
    relative_physical_stats INTEGER,
    time_of_day VARCHAR(50),
    trade_species_name VARCHAR(100),
    turn_upside_down BOOLEAN,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE pokemon_past_type (
    id BIGSERIAL PRIMARY KEY,
    pokemon_id BIGINT NOT NULL REFERENCES pokemon(id) ON DELETE CASCADE,
    generation_id BIGINT NOT NULL REFERENCES generation(id),
    slot INTEGER NOT NULL,
    type_id BIGINT NOT NULL REFERENCES pokemon_type(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (pokemon_id, generation_id, slot)
);

CREATE TABLE pokemon_type_assignment (
    id BIGSERIAL PRIMARY KEY,
    pokemon_id BIGINT NOT NULL REFERENCES pokemon(id) ON DELETE CASCADE,
    type_id BIGINT NOT NULL REFERENCES pokemon_type(id),
    slot INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (pokemon_id, slot),
    UNIQUE (pokemon_id, type_id)
);

CREATE TABLE pokemon_ability_assignment (
    id BIGSERIAL PRIMARY KEY,
    pokemon_id BIGINT NOT NULL REFERENCES pokemon(id) ON DELETE CASCADE,
    ability_id BIGINT NOT NULL REFERENCES pokemon_ability(id),
    slot INTEGER NOT NULL,
    is_hidden BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (pokemon_id, ability_id),
    UNIQUE (pokemon_id, slot)
);

CREATE TABLE pokemon_stat_value (
    id BIGSERIAL PRIMARY KEY,
    pokemon_id BIGINT NOT NULL REFERENCES pokemon(id) ON DELETE CASCADE,
    stat_id BIGINT NOT NULL REFERENCES pokemon_stat(id),
    base_stat INTEGER NOT NULL,
    effort INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (pokemon_id, stat_id)
);

CREATE TABLE pokemon_move_assignment (
    id BIGSERIAL PRIMARY KEY,
    pokemon_id BIGINT NOT NULL REFERENCES pokemon(id) ON DELETE CASCADE,
    move_id BIGINT NOT NULL REFERENCES pokemon_move(id),
    version_group_id BIGINT REFERENCES version_group(id),
    learn_method_name VARCHAR(100),
    level_learned_at INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (pokemon_id, move_id, version_group_id, learn_method_name, level_learned_at)
);

CREATE TABLE pokemon_sprite (
    id BIGSERIAL PRIMARY KEY,
    pokemon_id BIGINT NOT NULL REFERENCES pokemon(id) ON DELETE CASCADE,
    variant_key VARCHAR(100) NOT NULL,
    image_url TEXT,
    is_official_artwork BOOLEAN NOT NULL DEFAULT FALSE,
    is_shiny BOOLEAN NOT NULL DEFAULT FALSE,
    is_female BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (pokemon_id, variant_key)
);

CREATE INDEX idx_pokemon_species_id ON pokemon(species_id);
CREATE INDEX idx_pokemon_type_assignment_type ON pokemon_type_assignment(type_id);
CREATE INDEX idx_pokemon_ability_assignment_ability ON pokemon_ability_assignment(ability_id);
CREATE INDEX idx_pokemon_move_assignment_move ON pokemon_move_assignment(move_id);
CREATE INDEX idx_evolution_link_chain ON evolution_link(chain_id);
CREATE INDEX idx_evolution_link_to_species ON evolution_link(to_species_id);

COMMIT;
