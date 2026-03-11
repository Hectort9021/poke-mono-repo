package com.pokemon.ingestion.service;

import com.pokemon.ingestion.client.PokeApiClient;
import com.pokemon.ingestion.config.PokeApiProperties;
import com.pokemon.ingestion.dto.NamedApiResource;
import com.pokemon.ingestion.dto.PokemonListResponse;
import com.pokemon.ingestion.dto.PokemonResponse;
import com.pokemon.ingestion.persistence.PokemonEntity;
import com.pokemon.ingestion.persistence.PokemonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PokemonIngestionService {

    private static final Logger log = LoggerFactory.getLogger(PokemonIngestionService.class);

    private final PokeApiClient pokeApiClient;
    private final PokeApiProperties properties;
    private final PokemonRepository pokemonRepository;

    public PokemonIngestionService(
            PokeApiClient pokeApiClient,
            PokeApiProperties properties,
            PokemonRepository pokemonRepository
    ) {
        this.pokeApiClient = pokeApiClient;
        this.properties = properties;
        this.pokemonRepository = pokemonRepository;
    }

    public List<PokemonResponse> ingestPokemon(int maxPokemon) {
        int pageSize = properties.pageSize();
        int offset = 0;
        List<PokemonResponse> payload = new ArrayList<>();

        while (payload.size() < maxPokemon) {
            PokemonListResponse page = pokeApiClient.getPokemonPage(offset, pageSize);
            if (page == null || page.results() == null || page.results().isEmpty()) {
                break;
            }

            for (NamedApiResource resource : page.results()) {
                if (payload.size() >= maxPokemon) {
                    break;
                }

                PokemonResponse pokemon = pokeApiClient.getPokemonByName(resource.name());
                payload.add(pokemon);
                pokemonRepository.save(mapToEntity(pokemon));
                log.info("Pokémon ingestado: {} ({})", pokemon.name(), pokemon.id());
            }

            offset += pageSize;
            if (page.next() == null) {
                break;
            }
        }

        return payload;
    }

    public List<PokemonResponse> ingestAllPokemon() {
        return ingestPokemon(Integer.MAX_VALUE);
    }

    public long countStoredPokemon() {
        return pokemonRepository.count();
    }

    private PokemonEntity mapToEntity(PokemonResponse pokemon) {
        PokemonEntity entity = new PokemonEntity();
        entity.setPokeApiId(pokemon.id());
        entity.setName(pokemon.name());
        entity.setBaseExperience(pokemon.baseExperience());
        entity.setHeight(pokemon.height());
        entity.setWeight(pokemon.weight());
        entity.setIsDefault(pokemon.isDefault());
        return entity;
    }
}
