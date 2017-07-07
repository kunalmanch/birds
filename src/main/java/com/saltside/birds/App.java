package com.saltside.birds;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.saltside.birds.plumbing.ProductionModule;
import com.saltside.birds.utils.Path;
import com.saltside.birds.utils.PropertiesUtil;

import java.io.IOException;
import java.util.Properties;

import static com.saltside.birds.utils.Path.Route.*;
import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;
import static spark.Spark.*;

/**
 * Created by kunal on 7/5/2017.
 */
public class App {

    public static void start(BirdController birdController, int port) {
        port(port);

        // set up routes
        get(ONE_BIRD, birdController.getBird);
        get(BIRDS, birdController.getAllBirds);
        post(BIRDS, "application/json", birdController.addBird);
        delete(ONE_BIRD, birdController.deleteBird);

        // handle bad route
        get(ANY, (req, res) -> {
            res.type("application/json");
            res.status(NOT_FOUND_404);
            return "";
        });
        init();
    }

    public static void main(String[] args) throws IOException {
        Injector injector = Guice.createInjector(new ProductionModule());
        BirdController birdController = injector.getInstance(BirdController.class);

        Properties properties = PropertiesUtil.get(Path.Properties.APP_PROPERTIES);
        start(birdController, Integer.parseInt(properties.getProperty("port")));
    }

}
