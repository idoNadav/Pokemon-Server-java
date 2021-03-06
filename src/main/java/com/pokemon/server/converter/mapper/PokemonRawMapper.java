package com.plainid.server.converter.mapper;

import com.plainid.server.dao.Pokemon;
import com.plainid.server.dao.PokemonType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Hellper class to map SQL results to pokemon pojo
 */
public class PokemonRawMapper implements RowMapper<Pokemon> {
    @Override
    public Pokemon mapRow(ResultSet rs, int rowNum) throws SQLException {
        Pokemon pokemon = new Pokemon();
        pokemon.setId(rs.getInt("ID"));
        pokemon.setName(rs.getString("NAME"));
        pokemon.setType(Enum.valueOf(PokemonType.class, rs.getString("TYPE")));
        pokemon.setTrainerName(rs.getString("TRAINER_NAME"));
        return pokemon;
    }
}
