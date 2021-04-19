package com.plainid.server.services;


import com.plainid.server.Exception.PokemonException;
import com.plainid.server.dao.Pokemon;
import com.plainid.server.dao.PokemonList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PokemonService {

    private DbService dbService;

    @Autowired
    public PokemonService(DbService dbService) {
        this.dbService = dbService;
    }

    public Pokemon getPokemonById(int id) throws PokemonException {
        if (!dbService.isValidPokemonId(id)) {
            throw new PokemonException("The Pokemon id:" + " " + id + " " + "is not exist ");
        }
        return dbService.getPokemonById(id);
    }

    public Pokemon getPokemonByName(String pokemonName) throws PokemonException {
        if (!dbService.isValidPokemonName(pokemonName)) {
            throw new PokemonException("The Pokemon name:" + " " + pokemonName + " " + "is not exist ");
        }
        return dbService.getPokemonByName(pokemonName);
    }

    public PokemonList getAllPokemons() {
        PokemonList pokemonList = dbService.getAllPokemons();
        return pokemonList;
    }

}
