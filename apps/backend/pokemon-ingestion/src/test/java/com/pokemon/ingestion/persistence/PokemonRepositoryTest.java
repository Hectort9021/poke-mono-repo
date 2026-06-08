package com.pokemon.ingestion.persistence;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PokemonRepositoryTest {

    @Autowired
    private Flyway flyway;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PokemonRepository pokemonRepository;

    @Test
    void contextAppliesFlywayMigrationBeforeJpaValidation() {
        assertThat(flyway.info().current().getVersion().getVersion()).isEqualTo("1");

        Integer tableCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'POKEMON'",
                Integer.class
        );

        assertThat(tableCount).isEqualTo(1);
    }

    @Test
    void repositoryPersistsPokemonUsingMigratedSchema() {
        PokemonEntity pokemon = new PokemonEntity();
        pokemon.setPokeApiId(25);
        pokemon.setName("pikachu");
        pokemon.setBaseExperience(112);
        pokemon.setHeight(4);
        pokemon.setWeight(60);
        pokemon.setIsDefault(true);
        pokemon.setFrontDefaultSpriteUrl("https://sprites.example/pikachu.png");

        pokemonRepository.save(pokemon);

        assertThat(pokemonRepository.findById(25))
                .isPresent()
                .get()
                .extracting(PokemonEntity::getName)
                .isEqualTo("pikachu");
    }
}
