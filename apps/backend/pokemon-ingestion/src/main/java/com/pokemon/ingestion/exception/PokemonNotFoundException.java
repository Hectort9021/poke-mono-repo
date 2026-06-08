package com.pokemon.ingestion.exception;

public class PokemonNotFoundException extends RuntimeException {

    public PokemonNotFoundException(String pokemonName) {
        super("No se encontró el Pokémon: " + pokemonName);
    }

    public PokemonNotFoundException(String pokemonName, Throwable cause) {
        super("No se encontró el Pokémon: " + pokemonName, cause);
    }
}
