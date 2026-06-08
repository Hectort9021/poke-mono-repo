package com.pokemon.ingestion.exception;

public record ApiError(
        String error,
        String message
) {
}
