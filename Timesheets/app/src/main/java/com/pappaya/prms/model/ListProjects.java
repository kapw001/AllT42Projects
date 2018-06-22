package com.pappaya.prms.model;

/**
 * Created by yasar on 24/12/16.
 */
public class ListProjects {
    private String id,projectname;

    public ListProjects(String id, String projectname) {
        this.id = id;
        this.projectname = projectname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }
}
