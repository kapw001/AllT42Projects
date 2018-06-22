package com.pappaya.prms.model;

/**
 * Created by yasar on 10/12/16.
 */
public class User_id {
    private String id;

    private String name;

    public User_id() {

    }

    public User_id(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
