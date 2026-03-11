package com.pokemon.ingestion.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "pokeapi")
public record PokeApiProperties(
        @NotBlank String baseUrl,
        @Min(1) int pageSize
) {
}
