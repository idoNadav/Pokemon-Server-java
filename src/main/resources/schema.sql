CREATE TABLE POKEMON (ID INT PRIMARY KEY,
                    NAME VARCHAR(25),
                    TYPE VARCHAR(25),
                    TRAINER_NAME VARCHAR(30));


CREATE TABLE TRAINER (NAME VARCHAR(30) PRIMARY KEY,
                    LEVEL INT ,
                    POKEMONS_BAG VARCHAR(100));