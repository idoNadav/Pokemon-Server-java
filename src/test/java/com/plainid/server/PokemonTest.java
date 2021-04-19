package com.plainid.server;


import com.plainid.server.controller.parameters.CatchPokemonResponse;
import com.plainid.server.controller.parameters.TrainerResponse;
import com.plainid.server.dao.Battle;
import com.plainid.server.dao.Pokemon;
import com.plainid.server.dao.PokemonList;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class PokemonTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetAllPokemons() {
        PokemonList pokemonList = restTemplate.getForEntity("http://localhost:" + port + "/pokemon/list",
                PokemonList.class).getBody();
        assertThat(pokemonList).isNotNull();
        assertThat(pokemonList.getPokemons()).isNotNull();
    }

    @Test
    public void testGetPokemonById() {
        int id = 1;
        Pokemon pokemon = restTemplate.getForEntity("http://localhost:" + port + "/pokemon/{id}",
                Pokemon.class, id).getBody();
        assertThat(pokemon).isNotNull();
        assertThat(pokemon.getId()).isEqualTo(id);
        assertThat(pokemon.getName()).isNotEmpty();
        assertThat(pokemon.getTrainerName()).isNotEmpty();
    }


    @Test
    public void testGetPokemonByName() {
        String name = "Charizard";
        String type = "Fire";
        int id = 6;
        Pokemon pokemon = restTemplate.getForEntity("http://localhost:" + port + "/pokemon/name/{name}",
                Pokemon.class, name).getBody();
        assertThat(pokemon).isNotNull();
        assertThat(pokemon.getName()).isEqualTo(name);
        assertThat(pokemon.getId()).isEqualTo(id);
        assertThat(pokemon.getType().name()).isEqualTo(type);
    }

    @Test
    public void testGetSortedTrainerList() {
        List<TrainerResponse> trainerResponseList = restTemplate.getForEntity("http://localhost:" + port + "/trainers",
                List.class).getBody();
        assertThat(trainerResponseList).isNotNull();
    }

    @Test
    public void testWinBattle() {
        String trainer1Name = "Hash";
        String trainer2Name = "Brok";
        TrainerResponse trainer1beforeBattle = restTemplate.getForEntity("http://localhost:" + port + "/trainer/{trainerName}",
                TrainerResponse.class, trainer1Name).getBody();
        TrainerResponse trainer2beforeBattle = restTemplate.getForEntity("http://localhost:" + port + "/trainer/{trainerName}",
                TrainerResponse.class, trainer2Name).getBody();

        assertThat(trainer1beforeBattle).isNotNull();
        assertThat(trainer2beforeBattle).isNotNull();
        //level before battle:
        assertThat(trainer1beforeBattle.getLevel()).isEqualTo(2);
        assertThat(trainer2beforeBattle.getLevel()).isEqualTo(0);

        Battle battle = restTemplate.getForEntity("http://localhost:" + port + "/battle/{trainer1Name}/{trainer2Name}",
                Battle.class, trainer1Name, trainer2Name).getBody();
        assertThat(battle).isNotNull();
        assertThat(battle.getMessage()).isEqualTo("Brok wins!");
        assertThat(battle.getStatus()).isEqualTo("Success");

        TrainerResponse trainer1AfterBattle = restTemplate.getForEntity("http://localhost:" + port + "/trainer/{trainerName}",
                TrainerResponse.class, trainer1Name).getBody();
        TrainerResponse trainer2AfterBattle = restTemplate.getForEntity("http://localhost:" + port + "/trainer/{trainerName}",
                TrainerResponse.class, trainer2Name).getBody();
        //level after battle
        assertThat(trainer1AfterBattle.getLevel()).isEqualTo(2);
        assertThat(trainer2AfterBattle.getLevel()).isEqualTo(2);
    }

    @Test
    public void testTieBattle() {
        String trainer1Name = "Brok";
        String trainer2Name = "Rocket";
        TrainerResponse trainer1beforeBattle = restTemplate.getForEntity("http://localhost:" + port + "/trainer/{trainerName}",
                TrainerResponse.class, trainer1Name).getBody();
        TrainerResponse trainer2beforeBattle = restTemplate.getForEntity("http://localhost:" + port + "/trainer/{trainerName}",
                TrainerResponse.class, trainer2Name).getBody();

        assertThat(trainer1beforeBattle).isNotNull();
        assertThat(trainer2beforeBattle).isNotNull();
        //level before battle:
        assertThat(trainer1beforeBattle.getLevel()).isEqualTo(0);
        assertThat(trainer2beforeBattle.getLevel()).isEqualTo(1);

        Battle battle = restTemplate.getForEntity("http://localhost:" + port + "/battle/{trainer1Name}/{trainer2Name}",
                Battle.class, trainer1Name, trainer2Name).getBody();
        assertThat(battle).isNotNull();
        assertThat(battle.getStatus()).isEqualTo("Success");
        assertThat(battle.getMessage()).isEqualTo("Draw");


        TrainerResponse trainer1AfterBattle = restTemplate.getForEntity("http://localhost:" + port + "/trainer/{trainerName}",
                TrainerResponse.class, trainer1Name).getBody();
        TrainerResponse trainer2AfterBattle = restTemplate.getForEntity("http://localhost:" + port + "/trainer/{trainerName}",
                TrainerResponse.class, trainer2Name).getBody();
        //level after battle
        assertThat(trainer1AfterBattle.getLevel()).isEqualTo(1);
        assertThat(trainer2AfterBattle.getLevel()).isEqualTo(2);
    }

    @Test
    public void testCanceledBattle() {
        String trainer1Name = "Victoria";
        String trainer2Name = "Rocket";
        TrainerResponse trainer1beforeBattle = restTemplate.getForEntity("http://localhost:" + port + "/trainer/{trainerName}",
                TrainerResponse.class, trainer1Name).getBody();
        TrainerResponse trainer2beforeBattle = restTemplate.getForEntity("http://localhost:" + port + "/trainer/{trainerName}",
                TrainerResponse.class, trainer2Name).getBody();

        assertThat(trainer1beforeBattle).isNotNull();
        assertThat(trainer2beforeBattle).isNotNull();
        //level before battle:
        assertThat(trainer1beforeBattle.getLevel()).isEqualTo(0);
        assertThat(trainer2beforeBattle.getLevel()).isEqualTo(1);

        Battle battle = restTemplate.getForEntity("http://localhost:" + port + "/battle/{trainer1Name}/{trainer2Name}",
                Battle.class, trainer1Name, trainer2Name).getBody();
        assertThat(battle).isNotNull();
        assertThat(battle.getStatus()).isEqualTo("Error");
        assertThat(battle.getMessage()).isEqualTo("Canceled");


        TrainerResponse trainer1AfterBattle = restTemplate.getForEntity("http://localhost:" + port + "/trainer/{trainerName}",
                TrainerResponse.class, trainer1Name).getBody();
        TrainerResponse trainer2AfterBattle = restTemplate.getForEntity("http://localhost:" + port + "/trainer/{trainerName}",
                TrainerResponse.class, trainer2Name).getBody();
        //level after battle
        assertThat(trainer1AfterBattle.getLevel()).isEqualTo(0);
        assertThat(trainer2AfterBattle.getLevel()).isEqualTo(1);
    }

    @Test
    public void testGetPokemonsByTrainer() {
        String trainerName = "Hash";
        List<Pokemon> pokemonList = restTemplate.getForEntity("http://localhost:" + port + "/trainer/pokemons/{trainerName}",
                List.class, trainerName).getBody();
        assertThat(pokemonList).isNotNull();
        assertThat(pokemonList.size()).isEqualTo(3);
    }

    @Test
    public void testGetResponseTrainer() throws JSONException {
        String traineName = "Brok";
        int level = 0;
        List<String> bag = new ArrayList<String>(Arrays.asList("Cyndaquil", "Fennekin", "Vaporeon"));

        TrainerResponse trainerResponse = restTemplate.getForEntity("http://localhost:" + port + "/trainer/{trainerName}",
                TrainerResponse.class, traineName).getBody();
        assert trainerResponse != null;

        assertThat(trainerResponse).isNotNull();
        assertThat(trainerResponse.getName()).isEqualTo(traineName);
        assertThat(trainerResponse.getLevel()).isEqualTo(level);
        bag.forEach(p -> assertThat(trainerResponse.getBag().contains(p)));
    }

    @Test
    public void testSuccessPokemonCatch() {
        String trainerName = "Hash";
        String pokemonName = "Lapras";
        List<String> bagBeforeCatch = new ArrayList<String>(Arrays.asList("Charizard", "Magmar", "Victreebel"));

        TrainerResponse trainerBeforeCatch = restTemplate.getForEntity("http://localhost:" + port + "/trainer/{trainerName}",
                TrainerResponse.class, trainerName).getBody();
        assertThat(trainerBeforeCatch).isNotNull();
        bagBeforeCatch.forEach(p -> assertThat(trainerBeforeCatch.getBag().contains(p)));

        List<String> bagAfterCatch = new ArrayList<String>(Arrays.asList("Lapras", "Magmar", "Victreebel"));

        CatchPokemonResponse catchPokemonResponse = restTemplate.getForEntity("http://localhost:" + port + "/trainer/{trainerName}/catch/{pokemonName}",
                CatchPokemonResponse.class, trainerName, pokemonName).getBody();
        assertThat(catchPokemonResponse).isNotNull();

        TrainerResponse trainerAfterCatch = restTemplate.getForEntity("http://localhost:" + port + "/trainer/{trainerName}",
                TrainerResponse.class, trainerName).getBody();
        bagAfterCatch.forEach(p -> {
            assert trainerAfterCatch != null;
            assertThat(trainerAfterCatch.getBag().contains(p));
        });
    }

}
