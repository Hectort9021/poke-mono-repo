package com.pokemon.ingestion;

import com.pokemon.ingestion.config.PokeApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(PokeApiProperties.class)
public class PokemonIngestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokemonIngestionApplication.class, args);
    }
}
