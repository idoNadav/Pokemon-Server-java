package com.plainid.server.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class GsonUtils {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String jsonFromObject(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T typeTFromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }
}
