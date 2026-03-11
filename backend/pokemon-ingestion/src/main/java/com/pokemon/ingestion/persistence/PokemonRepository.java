package com.pokemon.ingestion.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonRepository extends JpaRepository<PokemonEntity, Integer> {
}
