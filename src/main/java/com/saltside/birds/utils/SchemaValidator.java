package com.saltside.birds.utils;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kunal on 7/5/2017.
 */
public class SchemaValidator {

    public static boolean validate(String json, String schemaLoc) throws IOException {
        boolean isValid = true;
        try (InputStream inputStream = SchemaValidator.class.getClassLoader().getResourceAsStream(schemaLoc)) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            Schema schema = SchemaLoader.load(rawSchema);
            schema.validate(new JSONObject(json));
        } catch (ValidationException|JSONException e) {
            isValid = false;
        }
        return isValid;
    }
}
