package com.saltside.birds.plumbing;

import com.github.fakemongo.Fongo;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.MongoClient;
import com.saltside.birds.utils.Path;
import com.saltside.birds.utils.PropertiesUtil;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by kunal on 7/5/2017.
 */
public class TestModule extends AbstractModule {

    protected void configure() {

    }

    @Provides
    Datastore provideDataStore() throws IOException {
        Properties properties = PropertiesUtil.get(Path.Properties.DB_PROPERTIES);
        Morphia morphia = new Morphia();
        Datastore datastore = morphia.createDatastore(new Fongo("mongo test server").getMongo(), properties.getProperty("db"));
        datastore.ensureIndexes();
        return datastore;
    }
}
