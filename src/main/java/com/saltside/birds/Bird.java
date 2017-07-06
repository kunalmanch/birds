package com.saltside.birds;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunal on 7/5/2017.
 */
@Entity("birds")
public class Bird {

    @Id
    private ObjectId id;
    private String name;
    private String family;
    private List<String> continents;
    private String added;
    private boolean visible;

    public Bird() {}

    public void setCreationDate(String added) {
        this.added = added;
    }

    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFamily() {
        return family;
    }

    public List<String> getContinents() {
        return continents;
    }

    public String getAdded() {
        return added;
    }

    public boolean isVisible() {
        return visible;
    }
}
