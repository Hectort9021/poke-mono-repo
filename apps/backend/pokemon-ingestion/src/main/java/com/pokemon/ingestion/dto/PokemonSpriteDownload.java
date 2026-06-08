package com.pokemon.ingestion.dto;

public record PokemonSpriteDownload(
        String filename,
        String contentType,
        byte[] content
) {
}
