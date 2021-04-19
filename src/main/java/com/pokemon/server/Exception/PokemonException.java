package com.plainid.server.Exception;

public class PokemonException extends Exception {

    public PokemonException(String msg){
        super(msg);
    }
    public PokemonException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
