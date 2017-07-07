package com.saltside.birds;

import com.google.inject.Inject;
import com.saltside.birds.utils.*;
import com.sun.media.sound.InvalidDataException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

import static com.saltside.birds.utils.Path.Schema.*;
import static org.eclipse.jetty.http.HttpStatus.*;

/**
 * Created by kunal on 7/5/2017.
 */
public class BirdController {

    private BirdDao birdDao;

    @Inject
    public BirdController(BirdDao birdDao) {
        this.birdDao = birdDao;
    }

    public Route getBird = (Request request, Response response) -> {
        String id = request.params(":id");
        String ret = "";
        try {
            Bird bird = birdDao.get(id);
            ret = JSONizer.birdToJSON(bird);
            response.status(OK_200);
            response.type("application/json");
        } catch (InvalidDataException|IllegalArgumentException e) {
            response.status(NOT_FOUND_404);
        }
        return ret;
    };

    public Route getAllBirds = (Request request, Response response) -> {
        response.type("application/json");
        List<Bird> birds = birdDao.getAll();
        response.status(OK_200);
        return JSONizer.birdsToJSONArray(birds);
    };

    public Route addBird = (Request request, Response response) -> {
        String payload = request.body();
        if (payload == null
                || payload.isEmpty()
                || !SchemaValidator.validate(payload, POST_REQUEST_JSON_SCHEMA)) {
            response.status(BAD_REQUEST_400);
            return "";
        }
        Bird newBird = JSONizer.jsonToBird(payload);
        newBird.setCreationDate(DateUtil.now());
        birdDao.add(newBird);
        response.status(CREATED_201);
        response.type("application/json");
        return JSONizer.birdToJSON(newBird);
    };

    public Route deleteBird = (Request request, Response response) -> {
        String id = request.params(":id");
        try {
            birdDao.delete(id);
            response.status(OK_200);
        } catch (InvalidDataException|IllegalArgumentException e) {
            response.status(NOT_FOUND_404);
        }
        return "";
    };
}
