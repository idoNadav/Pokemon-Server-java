package com.plainid.server.controller;


import com.plainid.server.Exception.PokemonException;
import com.plainid.server.dao.Pokemon;
import com.plainid.server.dao.PokemonList;
import com.plainid.server.services.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Pokemon controller
 */
@RestController
@RequestMapping("/")
public class PokemonController {

    private PokemonService pokemonService;

    @Autowired
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    /**
     * Get pokemon by give id
     *
     * @return pokemon.
     */
    @GetMapping("/pokemon/{id}")
    public Pokemon getPokemonById(@PathVariable int id) throws PokemonException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid input pokemon id: " + id);
        }
        return pokemonService.getPokemonById(id);
    }

    /**
     * Get pokemon by give name
     *
     * @return pokemon.
     */
    @GetMapping("/pokemon/name/{name}")
    public Pokemon getPokemonByName(@PathVariable String name) throws PokemonException {
        if (StringUtils.isEmpty(name)) {
            throw new PokemonException("Invalid input pokemon name:" + "" + name);
        }
        return pokemonService.getPokemonByName(name);
    }

    /**
     * Get all pokemons in the world
     *
     * @return List of pokemons in the world.
     */
    @GetMapping("/pokemon/list")
    public PokemonList getPokemons() {
        return pokemonService.getAllPokemons();
    }
}
