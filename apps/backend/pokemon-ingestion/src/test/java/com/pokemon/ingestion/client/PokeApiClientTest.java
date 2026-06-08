package com.pokemon.ingestion.client;

import com.pokemon.ingestion.config.PokeApiProperties;
import com.pokemon.ingestion.exception.PokemonNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

class PokeApiClientTest {

    @Test
    void getPokemonByName_throwsPokemonNotFoundWhenPokeApiReturnsNotFound() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        PokeApiClient client = new PokeApiClient(
                restClientBuilder,
                new PokeApiProperties("https://pokeapi.example/api/v2", 20)
        );
        server.expect(requestTo("https://pokeapi.example/api/v2/pokemon/missingno"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThatThrownBy(() -> client.getPokemonByName("missingno"))
                .isInstanceOf(PokemonNotFoundException.class)
                .hasMessage("No se encontró el Pokémon: missingno");

        server.verify();
    }
}
