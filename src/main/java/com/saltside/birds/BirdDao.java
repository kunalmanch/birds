package com.saltside.birds;

import com.google.inject.Inject;
import com.mongodb.WriteResult;
import com.saltside.birds.utils.DateUtil;
import com.sun.media.sound.InvalidDataException;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import java.util.List;
import java.util.Optional;

/**
 * Created by kunal on 7/5/2017.
 */
public class BirdDao {

    private Datastore ds;

    @Inject
    public BirdDao(Datastore ds) {
        this.ds = ds;
    }

    public void add(Bird bird) {
        ds.save(bird);
    }

    public List<Bird> getAll() {
        List<Bird> birds = ds.createQuery(Bird.class).asList();
        return birds;
    }

    public Bird get(String id) throws InvalidDataException, IllegalArgumentException {
        ObjectId objectId = new ObjectId(id);
        return Optional.ofNullable(ds.get(Bird.class, objectId)).orElseThrow(InvalidDataException::new);
    }

    public void delete(String id) throws InvalidDataException, IllegalArgumentException {
        ObjectId objectId = new ObjectId(id);
        WriteResult writeResult = ds.delete(Bird.class, objectId);
        if (writeResult.getN() != 1) throw new InvalidDataException();
    }
}
