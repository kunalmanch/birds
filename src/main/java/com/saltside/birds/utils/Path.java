package com.saltside.birds.utils;

/**
 * Created by kunal on 7/5/2017.
 */
public class Path {

    public static class Properties {
        public static final String DB_PROPERTIES = "db.properties";
        public static final String APP_PROPERTIES = "app.properties";
    }

    public static class Route {
        public static final String BIRDS = "/birds";
        public static final String ONE_BIRD = "/birds/:id";
        public static final String ANY = "*";
    }

    public static class Schema {
        public static final String POST_REQUEST_JSON_SCHEMA = "json_schemas/post-birds-request.json";
        public static final String POST_RESPONSE_JSON_SCHEMA = "json_schemas/post-birds-response.json";
        public static final String GET_ALL_RESPONSE_JSON_SCHEMA = "json_schemas/get-birds-response.json";
        public static final String GET_RESPONSE_JSON_SCHEMA = "json_schemas/get-birds-id-response.json";
    }
}
