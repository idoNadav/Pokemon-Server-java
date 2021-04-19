package com.plainid.server.controller;


import com.plainid.server.Exception.PokemonException;
import com.plainid.server.controller.parameters.CatchPokemonResponse;
import com.plainid.server.controller.parameters.TrainerResponse;
import com.plainid.server.dao.Battle;
import com.plainid.server.dao.Pokemon;
import com.plainid.server.services.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

/**
 * Trainer controller
 */
@RestController
@RequestMapping("/")
public class TrainerController {

    private TrainerService trainerService;

    @Autowired
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    /**
     * Get trainer with his pokemons name.
     *
     * @return trainer details.
     */
    @GetMapping("/trainer/{trainerName}")
    public TrainerResponse getResponseTrainer(@PathVariable String trainerName) throws PokemonException {
        if (StringUtils.isEmpty(trainerName)) {
            throw new PokemonException("Invalid trainer name:" + " " + trainerName);
        }
        return trainerService.getResponseTrainer(trainerName);
    }

    /**
     * Get all trainer pokemons by trainer name.
     *
     * @return all trainer pokemons.
     */
    @GetMapping("/trainer/pokemons/{trainerName}")
    public List<Pokemon> getPokemonsByTrainer(@PathVariable String trainerName) throws PokemonException {
        if (StringUtils.isEmpty(trainerName)) {
            throw new PokemonException("Invalid trainer name:" + " " + trainerName);
        }
        return trainerService.getPokemonsByTrainer(trainerName);
    }

    /**
     * Catch pokemon.
     * The catching succeeded only if the Pokemon were free.
     *
     * @return new trainer pokemons bag.
     */
    @GetMapping("/trainer/{trainerName}/catch/{pokemonName}")
    public CatchPokemonResponse pokemonCatch(@PathVariable String trainerName, @PathVariable String pokemonName) throws PokemonException {
        if (StringUtils.isEmpty(trainerName) || StringUtils.isEmpty(pokemonName)) {
            throw new PokemonException("Invalid input");
        }
        if (!trainerService.pokemonCatch(trainerName, pokemonName)) {
            throw new PokemonException("Catching failed !" + " " + pokemonName + " is owned ");
        }
        TrainerResponse trainerResponse = trainerService.getResponseTrainer(trainerName);
        CatchPokemonResponse catchPokemonResponse = new CatchPokemonResponse();
        catchPokemonResponse.setBag(trainerResponse.getBag());
        return catchPokemonResponse;
    }

    /**
     * Battle between two trainers.
     *
     * @return the winner of the battle or if there is a tie or if the battle is canceled
     */
    @GetMapping("/battle/{trainer1Name}/{trainer2Name}")
    public Battle battle(@PathVariable String trainer1Name, @PathVariable String trainer2Name) throws PokemonException {
        if (StringUtils.isEmpty(trainer1Name) || StringUtils.isEmpty(trainer2Name)) {
            throw new PokemonException("Invalid input");
        }
        return trainerService.fight(trainer1Name, trainer2Name);
    }

    /**
     *get all trainers in the world .
     * sorted by Sorted by their level.
     *
     * @return new trainer pokemons bag.
     */
    @GetMapping("/trainers")
    public List<TrainerResponse> getSortedTrainerList() throws PokemonException {
        return trainerService.getSortedTrainerList();
    }
}
