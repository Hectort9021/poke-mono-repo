package com.pokemon.ingestion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PokemonResponse(
        int id,
        String name,
        @JsonProperty("base_experience") Integer baseExperience,
        int height,
        int weight,
        @JsonProperty("is_default") boolean isDefault,
        PokemonSprites sprites,
        List<PokemonTypeSlot> types,
        List<PokemonStatSlot> stats,
        List<PokemonAbilitySlot> abilities,
        List<PokemonMoveSlot> moves,
        NamedApiResource species
) {

    public record PokemonSprites(
            @JsonProperty("front_default") String frontDefault,
            @JsonProperty("front_shiny") String frontShiny,
            @JsonProperty("back_default") String backDefault,
            @JsonProperty("back_shiny") String backShiny
    ) {
    }

    public record PokemonTypeSlot(
            int slot,
            NamedApiResource type
    ) {
    }

    public record PokemonStatSlot(
            int effort,
            @JsonProperty("base_stat") int baseStat,
            NamedApiResource stat
    ) {
    }

    public record PokemonAbilitySlot(
            @JsonProperty("is_hidden") boolean hidden,
            int slot,
            NamedApiResource ability
    ) {
    }

    public record PokemonMoveSlot(
            NamedApiResource move
    ) {
    }
}
