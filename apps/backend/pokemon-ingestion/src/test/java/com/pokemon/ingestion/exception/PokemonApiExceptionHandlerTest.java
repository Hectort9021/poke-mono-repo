package com.pokemon.ingestion.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class PokemonApiExceptionHandlerTest {

    private final PokemonApiExceptionHandler handler = new PokemonApiExceptionHandler();

    @Test
    void handlePokemonNotFound_returnsNotFoundError() {
        var response = handler.handlePokemonNotFound(new PokemonNotFoundException("missingno"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(new ApiError(
                "pokemon_not_found",
                "No se encontró el Pokémon: missingno"
        ));
    }

    @Test
    void handlePokemonSpriteNotFound_returnsNotFoundError() {
        var response = handler.handlePokemonSpriteNotFound(new PokemonSpriteNotFoundException("pikachu"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(new ApiError(
                "pokemon_sprite_not_found",
                "No se encontró sprite front_default para el Pokémon: pikachu"
        ));
    }
}
