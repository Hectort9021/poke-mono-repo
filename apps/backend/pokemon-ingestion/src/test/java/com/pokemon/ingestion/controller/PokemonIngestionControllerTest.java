package com.pokemon.ingestion.controller;

import com.pokemon.ingestion.dto.PokemonResponse;
import com.pokemon.ingestion.service.PokemonIngestionService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PokemonIngestionControllerTest {

    @Test
    void ingestPokemon_callsAllModeWhenFlagIsTrue() {
        PokemonIngestionService service = mock(PokemonIngestionService.class);
        PokemonIngestionController controller = new PokemonIngestionController(service);
        when(service.ingestAllPokemon()).thenReturn(List.of());

        List<PokemonResponse> result = controller.ingestPokemon(20, true);

        assertThat(result).isEmpty();
        verify(service).ingestAllPokemon();
    }

    @Test
    void ingestPokemon_callsLimitedModeWhenFlagIsFalse() {
        PokemonIngestionService service = mock(PokemonIngestionService.class);
        PokemonIngestionController controller = new PokemonIngestionController(service);
        when(service.ingestPokemon(10)).thenReturn(List.of());

        List<PokemonResponse> result = controller.ingestPokemon(10, false);

        assertThat(result).isEmpty();
        verify(service).ingestPokemon(10);
    }

    @Test
    void countInMemoryPokemon_returnsCurrentValue() {
        PokemonIngestionService service = mock(PokemonIngestionService.class);
        PokemonIngestionController controller = new PokemonIngestionController(service);
        when(service.countStoredPokemon()).thenReturn(42L);

        long result = controller.countInMemoryPokemon();

        assertThat(result).isEqualTo(42L);
        verify(service).countStoredPokemon();
    }
}
