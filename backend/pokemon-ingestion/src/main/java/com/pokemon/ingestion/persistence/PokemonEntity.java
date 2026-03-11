package com.pokemon.ingestion.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pokemon")
public class PokemonEntity {

    @Id
    @Column(name = "pokeapi_id", nullable = false)
    private Integer pokeApiId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "base_experience")
    private Integer baseExperience;

    @Column(nullable = false)
    private Integer height;

    @Column(nullable = false)
    private Integer weight;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    public Integer getPokeApiId() {
        return pokeApiId;
    }

    public void setPokeApiId(Integer pokeApiId) {
        this.pokeApiId = pokeApiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBaseExperience() {
        return baseExperience;
    }

    public void setBaseExperience(Integer baseExperience) {
        this.baseExperience = baseExperience;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
}
