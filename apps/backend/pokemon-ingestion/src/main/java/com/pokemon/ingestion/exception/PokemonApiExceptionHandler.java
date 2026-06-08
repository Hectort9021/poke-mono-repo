package com.pokemon.ingestion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PokemonApiExceptionHandler {

    @ExceptionHandler(PokemonNotFoundException.class)
    public ResponseEntity<ApiError> handlePokemonNotFound(PokemonNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("pokemon_not_found", exception.getMessage()));
    }

    @ExceptionHandler(PokemonSpriteNotFoundException.class)
    public ResponseEntity<ApiError> handlePokemonSpriteNotFound(PokemonSpriteNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("pokemon_sprite_not_found", exception.getMessage()));
    }
}
