package com.pokemon.ingestion.dto;

import java.util.List;

public record PokemonListResponse(
        int count,
        String next,
        String previous,
        List<NamedApiResource> results
) {
}
