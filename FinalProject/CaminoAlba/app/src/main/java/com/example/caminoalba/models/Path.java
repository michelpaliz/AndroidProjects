package com.example.caminoalba.models;

import java.io.Serializable;

public class Path implements Serializable {

    private int id;
    private String name;
    private String information;
    private String photo;
    private boolean credential;

    public Path(int id, String name, String information, String photo, boolean credential) {
        this.id = id;
        this.name = name;
        this.information = information;
        this.photo = photo;
        this.credential = credential;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCredential() {
        return credential;
    }

    public void setCredential(boolean credential) {
        this.credential = credential;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    @Override
    public String toString() {
        return "Path{" +
                "name='" + name + '\'' +
                ", information='" + information + '\'' +
                ", photo='" + photo + '\'' +
                ", credential=" + credential +
                '}';
    }
}
