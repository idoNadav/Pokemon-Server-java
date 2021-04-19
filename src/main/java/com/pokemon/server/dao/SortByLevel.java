package com.plainid.server.dao;

import java.util.Comparator;

public class SortByLevel implements Comparator<Trainer> {

    public int compare(Trainer a, Trainer b) {
        return a.getLevel() - b.getLevel();
    }
}
