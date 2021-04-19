package com.plainid.server.converter.mapper;

import com.plainid.server.dao.Trainer;
import com.plainid.server.utils.GsonUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


public class TrainerRawMapper implements RowMapper<Trainer> {

    @Override
    public Trainer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Trainer trainer = new Trainer();
        trainer.setName(resultSet.getString("Name"));
        trainer.setLevel(resultSet.getInt("LEVEL"));
        List<Double> pokemonsBagAsDouble = GsonUtils.typeTFromJson(resultSet.getString("POKEMONS_BAG"), List.class);
        //known Gson problem - deserialize number to double so casting needed.
        List<Integer> pokemonsBag = pokemonsBagAsDouble.stream().map(Double::intValue).collect(Collectors.toList());
        trainer.setPokemonsBag(pokemonsBag);
        return trainer;
    }
}
