package com.plainid.server.services;

import com.plainid.server.converter.mapper.PokemonRawMapper;
import com.plainid.server.converter.mapper.TrainerRawMapper;
import com.plainid.server.dao.Pokemon;
import com.plainid.server.dao.PokemonList;
import com.plainid.server.dao.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DbService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Trainer getTrainer(String trainerName) {
        String sql = "SELECT * FROM trainer WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{trainerName}, new TrainerRawMapper());
    }

    public List<Pokemon> getPokemonsByTrainer(String trainerName) {
        String sql = "SELECT* FROM pokemon WHERE trainer_name=?";
        return jdbcTemplate.query(sql, new Object[]{trainerName}, new PokemonRawMapper());
    }

    public Pokemon getPokemonByName(String pokemonName) {
        String sql = "SELECT* FROM pokemon WHERE NAME=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{pokemonName}, new PokemonRawMapper());
    }

    public Pokemon getPokemonById(int id) {
        String sql = "SELECT* FROM pokemon WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new PokemonRawMapper());
    }

    public PokemonList getAllPokemons() {
        List<Pokemon> rows = jdbcTemplate.query("SELECT * FROM pokemon", new PokemonRawMapper());
        PokemonList pokemonList = new PokemonList();
        pokemonList.setPokemons(rows);
        return pokemonList;
    }

    public List<Trainer> getAllTrainers() {
        List<Trainer> rows = jdbcTemplate.query("SELECT* FROM trainer", new TrainerRawMapper());
        List<Trainer> trainerList = new ArrayList<>();
        trainerList = rows;
        return trainerList;
    }

    public boolean isValidTrainer(String trainerNme) {
        String sql = "SELECT count(*) FROM trainer WHERE name=?";
        return (jdbcTemplate.queryForObject(sql, new Object[]{trainerNme}, Integer.class) > 0);
    }

    public boolean isValidPokemonId(int id) {
        String sql = "SELECT count(*) FROM pokemon WHERE id=?";
        return (jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class) > 0);
    }

    public boolean isValidPokemonName(String pokemonName) {
        String sql = "SELECT count(*) FROM pokemon WHERE name=?";
        return (jdbcTemplate.queryForObject(sql, new Object[]{pokemonName}, Integer.class) > 0);
    }

    public boolean isPokemonFree(int id) {
        String sql = "SELECT* FROM pokemon WHERE id=?";
        Pokemon pokemon = jdbcTemplate.queryForObject(sql, new Object[]{id}, new PokemonRawMapper());
        assert pokemon != null;
        return pokemon.getTrainerName() == null;
    }

    public void updateTrainerLevel(String trainerName, int points) {
        String sql = "UPDATE trainer SET level=? WHERE name=?";
        jdbcTemplate.update(sql, points, trainerName);
    }

    public void updatePokemonTrainerName(int newPokemonId, int oldPokemonId, String trainerName) {
        String sql1 = "UPDATE pokemon SET trainer_name=?  WHere Id=?";
        String sql2 = "UPDATE pokemon SET trainer_name=? WHERE ID=?";
        jdbcTemplate.update(sql1, trainerName, newPokemonId);
        jdbcTemplate.update(sql2, null, oldPokemonId);
    }

    public void updateTrainerBag(String trainerName, String bag) {
        String sql = "UPDATE trainer SET pokemons_bag=? WHERE name=?";
        jdbcTemplate.update(sql, bag, trainerName);
    }
}
