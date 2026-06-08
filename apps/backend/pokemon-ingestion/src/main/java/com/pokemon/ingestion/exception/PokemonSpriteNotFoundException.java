package com.pokemon.ingestion.exception;

public class PokemonSpriteNotFoundException extends RuntimeException {

    public PokemonSpriteNotFoundException(String pokemonName) {
        super("No se encontró sprite front_default para el Pokémon: " + pokemonName);
    }
}
