package com.plainid.server.dao;

import java.util.List;

public class Trainer {

    private String name;
    private int level;
    private List<Integer> pokemonsBag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Integer> getPokemonsBag() {
        return pokemonsBag;
    }

    public void setPokemonsBag(List<Integer> pokemonsBag) {
        this.pokemonsBag = pokemonsBag;
    }
}
