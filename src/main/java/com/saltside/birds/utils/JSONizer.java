package com.saltside.birds.utils;

import com.google.gson.*;
import com.saltside.birds.Bird;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by kunal on 7/5/2017.
 */
public class JSONizer {

    public static String birdToJSON(Bird bird) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Bird.class, new BirdSerializer());
        return gsonBuilder.create().toJson(bird);
    }

    public static Bird jsonToBird(String json) {
        return new Gson().fromJson(json, Bird.class);
    }

    public static String birdsToJSONArray(List<Bird> birds) {
        JsonArray birdIds = new JsonArray();
        for (Bird bird : birds) {
            if (bird.isVisible()) continue;
            birdIds.add(bird.getId().toHexString());
        }

        return birdIds.toString();
    }


    private static class BirdSerializer implements JsonSerializer<Bird> {

        @Override
        public JsonElement serialize(Bird bird, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id", bird.getId().toHexString());
            obj.addProperty("name", bird.getName());
            obj.addProperty("family", bird.getFamily());
            obj.addProperty("added", bird.getAdded());
            obj.addProperty("visible", bird.isVisible());
            JsonArray continents = new JsonArray();
            for (String continent : bird.getContinents()) {
                continents.add(continent);
            }
            obj.add("continents", continents);

            return obj;
        }
    }

}
