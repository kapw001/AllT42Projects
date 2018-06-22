package com.pappaya.prms.model;

/**
 * Created by yasar on 16/12/16.
 */
public class Employee_id {

    private String id;

    private String name;

    public Employee_id() {
    }

    public Employee_id(String id, String name) {
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
