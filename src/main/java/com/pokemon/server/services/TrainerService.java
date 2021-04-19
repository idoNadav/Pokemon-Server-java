package com.plainid.server.services;

import com.plainid.server.Exception.PokemonException;
import com.plainid.server.dao.SortByLevel;
import com.plainid.server.controller.parameters.TrainerResponse;
import com.plainid.server.dao.Battle;
import com.plainid.server.dao.Pokemon;
import com.plainid.server.dao.Trainer;
import com.plainid.server.utils.GsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Trainer service
 */
@Service
public class TrainerService {

    private DbService dbService;

    @Autowired
    public TrainerService(DbService dbService) {
        this.dbService = dbService;
    }

    /**
     * Get trainer with his pokemons bag.
     *
     * @return trainer details.
     */
    public TrainerResponse getResponseTrainer(String trainerName) throws PokemonException {
        if (!dbService.isValidTrainer(trainerName)) {
            throw new PokemonException("The trainer:" + " " + trainerName + " " + "is not exist ");
        }
        Trainer trainer = dbService.getTrainer(trainerName);
        TrainerResponse trainerResponse = new TrainerResponse();
        trainerResponse.setName(trainerName);
        trainerResponse.setLevel(trainer.getLevel());
        trainerResponse.setBag(getPokemonsName(trainerName));
        return trainerResponse;
    }

    /**
     * Get trainer with his pokemons id's.
     *
     * @return trainer details.
     */
    public Trainer getTrainer(String trainerName) throws PokemonException {
        if (!dbService.isValidTrainer(trainerName)) {
            throw new PokemonException("The trainer:" + " " + trainerName + " " + "is not exist ");
        }
        return dbService.getTrainer(trainerName);
    }

    /**
     * Get list of all trainers in the world , sored by their level.
     *
     * @return all trainers
     */
    public List<TrainerResponse> getSortedTrainerList() throws PokemonException {
        List<Trainer> trainerList = dbService.getAllTrainers();
        trainerList.sort(new SortByLevel());
        List<TrainerResponse> trainerResponseList = new ArrayList<>();
        TrainerResponse trainerResponse = new TrainerResponse();
        for (Trainer t : trainerList) {
            trainerResponse = getResponseTrainer(t.getName());
            trainerResponseList.add(trainerResponse);
        }
        return trainerResponseList;
    }

    /**
     * Try to catch new pokemon.
     * The catching succeeded only if the Pokemon were free.
     *
     * @return true if the catching success and false if the pokemon is owned.
     */
    public boolean pokemonCatch(String trainerName, String pokemonName) throws PokemonException {
        if (!dbService.isValidTrainer(trainerName)) {
            throw new PokemonException("The trainer:" + " " + trainerName + " " + "is not exist ");
        } else if (!dbService.isValidPokemonName(pokemonName)) {
            throw new PokemonException("The pokemon:" + " " + pokemonName + " " + "is not exist ");
        }
        Pokemon newPokemon = dbService.getPokemonByName(pokemonName);
        if (!dbService.isPokemonFree(newPokemon.getId())) {
            return false;
        }
        Trainer trainer = getTrainer(trainerName);
        if(trainer.getPokemonsBag().isEmpty()){
            addPokemonToBag(trainer, newPokemon);
            String listString = GsonUtils.jsonFromObject(trainer.getPokemonsBag());
            dbService.updateTrainerBag(trainer.getName(), listString);
        }else {
            Pokemon oldestPokemon = dbService.getPokemonById(trainer.getPokemonsBag().get(0));
            addPokemonToBag(trainer, newPokemon);
            String listString = GsonUtils.jsonFromObject(trainer.getPokemonsBag());
            dbService.updatePokemonTrainerName(newPokemon.getId(), oldestPokemon.getId(), trainer.getName());
            dbService.updateTrainerBag(trainer.getName(), listString);
        }
        return true;
    }

    /**
     * Get list Of all the trainer's Pokemon
     *
     * @return pokemons list.
     */
    public List<Pokemon> getPokemonsByTrainer(String trainerName) throws PokemonException {
        if (!dbService.isValidTrainer(trainerName)) {
            throw new PokemonException("The trainer:" + " " + trainerName + " " + "is not exist ");
        }
        return dbService.getPokemonsByTrainer(trainerName);
    }

    /**
     * Battle between two trainers.
     *
     * @return the winner of the battle or if there is a tie or if the battle is canceled
     */
    public Battle fight(String trainerName1, String trainerName2) throws PokemonException {
        if (!dbService.isValidTrainer(trainerName1)) {
            throw new PokemonException("The trainer:" + " " + trainerName1 + " " + "is not exist ");
        }
        if (!dbService.isValidTrainer(trainerName2)) {
            throw new PokemonException("The trainer:" + " " + trainerName2 + " " + "is not exist ");
        }
        Trainer trainer1 = dbService.getTrainer(trainerName1);
        Trainer trainer2 = dbService.getTrainer(trainerName2);
        int pointsTrainer1 = 0, pointsTrainer2 = 0;
        Battle battle = new Battle();

        if (trainer1.getPokemonsBag().size() != trainer2.getPokemonsBag().size() || trainer1.getPokemonsBag().size() < 3 ||
                trainer2.getPokemonsBag().size() < 3) {
            battle.setStatus("Error");
            battle.setMessage("Canceled");
            return battle;
        }
        List<Pokemon> pokemonsListTrainer1 = dbService.getPokemonsByTrainer(trainerName1);
        List<Pokemon> pokemonsListTrainer2 = dbService.getPokemonsByTrainer(trainerName2);
        for (int i = 0; i < 3; i++) {
            Pokemon pokemonTrainer1 = pokemonsListTrainer1.get(i);
            Pokemon pokemonTrainer2 = pokemonsListTrainer2.get(i);
            switch (pokemonTrainer1.getType().name()) {
                case "Fire":
                    switch (pokemonTrainer2.getType().name()) {
                        case "Fire":
                            pointsTrainer1 += 1;
                            pointsTrainer2 += 1;
                            break;
                        case "Water":
                            pointsTrainer2 += 1;
                            break;
                        case "Grass":
                            pointsTrainer1 += 1;
                            break;
                    }
                case "Water":
                    switch (pokemonTrainer2.getType().name()) {
                        case "Fire":
                            pointsTrainer1 += 1;
                            break;
                        case "Water":
                            pointsTrainer2 += 1;
                            pointsTrainer1 += 1;
                            break;
                        case "Grass":
                            pointsTrainer2 += 1;
                            break;
                    }
                case "Grass":
                    switch (pokemonTrainer2.getType().name()) {
                        case "Fire":
                            pointsTrainer2 += 1;
                            break;
                        case "Water":
                            pointsTrainer1 += 1;
                            break;
                        case "Grass":
                            pointsTrainer2 += 1;
                            pointsTrainer1 += 1;
                            break;
                    }
            }
        }
        if (pointsTrainer1 == pointsTrainer2) {
            battle.setStatus("Success");
            battle.setMessage("Draw");
            dbService.updateTrainerLevel(trainerName1, trainer1.getLevel() + 1);
            dbService.updateTrainerLevel(trainerName2, trainer2.getLevel() + 1);
            return battle;
        }

        if (pointsTrainer1 > pointsTrainer2) {
            battle.setStatus("Success");
            battle.setMessage(trainer1.getName() + " " + "wins!");
            dbService.updateTrainerLevel(trainerName1, trainer1.getLevel() + 2);
            return battle;
        }
        battle.setStatus("Success");
        battle.setMessage(trainer2.getName() + " " + "wins!");
        dbService.updateTrainerLevel(trainerName2, trainer2.getLevel() + 2);
        return battle;
    }

    /**
     * Add new pokemon to trainer bag.
     *If the bag is full then the oldest pokemon is removed.
     * The oldest Pokemon will always be the first value in the list.
     */
    public void addPokemonToBag(Trainer trainer, Pokemon pokemon) {
        pokemon.setTrainerName(trainer.getName());
        List<Integer> newBag = new ArrayList<>();
        if (trainer.getPokemonsBag().size() >= 3) {
            newBag.add(0, trainer.getPokemonsBag().get(1));
            newBag.add(1, trainer.getPokemonsBag().get(2));
            newBag.add(2, pokemon.getId());
            trainer.setPokemonsBag(newBag);
        } else {
            trainer.getPokemonsBag().add(pokemon.getId());
        }
    }

    /**
     * Get all pokemons name in the bag by trainer.
     *
     * @return list of pokemons name.
     */
    public List<String> getPokemonsName(String trainerName) {
        List<Integer> pokemonsIds = dbService.getTrainer(trainerName).getPokemonsBag();
        List<String> pokemonsName = new ArrayList<>();
        for (int i = 0; i < pokemonsIds.size(); i++) {
            pokemonsName.add(dbService.getPokemonById(pokemonsIds.get(i)).getName());
        }
        return pokemonsName;
    }
}
