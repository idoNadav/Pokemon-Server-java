package com.plainid.server.controller.parameters;

import java.util.List;

public class TrainerResponse {

    private String name;
    private int level;
    private List<String> bag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<String> getBag() {
        return bag;
    }

    public void setBag(List<String> bag) {
        this.bag = bag;
    }

    @Override
    public String toString() {
        return "TrainerResponse{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", pokemonsBag=" + bag +
                '}';
    }
}
