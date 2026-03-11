package com.pokemon.ingestion.client;

import com.pokemon.ingestion.config.PokeApiProperties;
import com.pokemon.ingestion.dto.PokemonListResponse;
import com.pokemon.ingestion.dto.PokemonResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PokeApiClient {

    private final RestClient restClient;

    public PokeApiClient(RestClient.Builder restClientBuilder, PokeApiProperties properties) {
        this.restClient = restClientBuilder
                .baseUrl(properties.baseUrl())
                .build();
    }

    public PokemonListResponse getPokemonPage(int offset, int limit) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/pokemon")
                        .queryParam("offset", offset)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .body(PokemonListResponse.class);
    }

    public PokemonResponse getPokemonByName(String name) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/pokemon/{name}")
                        .build(name))
                .retrieve()
                .body(PokemonResponse.class);
    }
}
