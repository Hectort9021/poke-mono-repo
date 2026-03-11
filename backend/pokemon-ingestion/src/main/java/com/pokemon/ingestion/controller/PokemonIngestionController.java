package com.pokemon.ingestion.controller;

import com.pokemon.ingestion.dto.PokemonResponse;
import com.pokemon.ingestion.service.PokemonIngestionService;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/ingestion")
public class PokemonIngestionController {

    private final PokemonIngestionService ingestionService;

    public PokemonIngestionController(PokemonIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @GetMapping("/pokemon")
    public List<PokemonResponse> ingestPokemon(
            @RequestParam(defaultValue = "20") @Min(1) int limit,
            @RequestParam(defaultValue = "false") boolean all
    ) {
        if (all) {
            return ingestionService.ingestAllPokemon();
        }
        return ingestionService.ingestPokemon(limit);
    }

    @GetMapping("/pokemon/count")
    public long countInMemoryPokemon() {
        return ingestionService.countStoredPokemon();
    }
}
