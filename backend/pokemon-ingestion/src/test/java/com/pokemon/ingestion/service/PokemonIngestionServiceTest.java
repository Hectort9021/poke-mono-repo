package com.pokemon.ingestion.service;

import com.pokemon.ingestion.client.PokeApiClient;
import com.pokemon.ingestion.config.PokeApiProperties;
import com.pokemon.ingestion.dto.NamedApiResource;
import com.pokemon.ingestion.dto.PokemonListResponse;
import com.pokemon.ingestion.dto.PokemonResponse;
import com.pokemon.ingestion.persistence.PokemonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonIngestionServiceTest {

    @Mock
    private PokeApiClient pokeApiClient;

    @Mock
    private PokeApiProperties properties;

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private PokemonIngestionService ingestionService;

    @Test
    void ingestPokemon_returnsOnlyRequestedLimit() {
        when(properties.pageSize()).thenReturn(2);
        when(pokeApiClient.getPokemonPage(0, 2)).thenReturn(new PokemonListResponse(
                3,
                "next",
                null,
                List.of(
                        new NamedApiResource("bulbasaur", "u1"),
                        new NamedApiResource("ivysaur", "u2")
                )
        ));
        when(pokeApiClient.getPokemonByName("bulbasaur")).thenReturn(pokemon(1, "bulbasaur"));

        List<PokemonResponse> result = ingestionService.ingestPokemon(1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("bulbasaur");
        verify(pokemonRepository).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void ingestAllPokemon_readsUntilNoNextPage() {
        when(properties.pageSize()).thenReturn(2);
        when(pokeApiClient.getPokemonPage(0, 2)).thenReturn(new PokemonListResponse(
                4,
                "next",
                null,
                List.of(
                        new NamedApiResource("bulbasaur", "u1"),
                        new NamedApiResource("ivysaur", "u2")
                )
        ));
        when(pokeApiClient.getPokemonPage(2, 2)).thenReturn(new PokemonListResponse(
                4,
                null,
                null,
                List.of(
                        new NamedApiResource("venusaur", "u3"),
                        new NamedApiResource("charmander", "u4")
                )
        ));

        when(pokeApiClient.getPokemonByName("bulbasaur")).thenReturn(pokemon(1, "bulbasaur"));
        when(pokeApiClient.getPokemonByName("ivysaur")).thenReturn(pokemon(2, "ivysaur"));
        when(pokeApiClient.getPokemonByName("venusaur")).thenReturn(pokemon(3, "venusaur"));
        when(pokeApiClient.getPokemonByName("charmander")).thenReturn(pokemon(4, "charmander"));

        List<PokemonResponse> result = ingestionService.ingestAllPokemon();

        assertThat(result).hasSize(4);
        assertThat(result).extracting(PokemonResponse::name)
                .containsExactly("bulbasaur", "ivysaur", "venusaur", "charmander");
    }

    @Test
    void countStoredPokemon_returnsRepositoryCount() {
        when(pokemonRepository.count()).thenReturn(151L);

        long result = ingestionService.countStoredPokemon();

        assertThat(result).isEqualTo(151L);
    }

    private PokemonResponse pokemon(int id, String name) {
        return new PokemonResponse(id, name, null, 0, 0, true, List.of(), List.of(), List.of(), List.of(), null);
    }
}
