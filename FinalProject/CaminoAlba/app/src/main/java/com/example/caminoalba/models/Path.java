package com.example.caminoalba.models;

import java.io.Serializable;

public class Path implements Serializable {

    private int id;
    private String name;
    private String information;
    private String photo;
    private int orden;
    private boolean credential;

    public Path(int id, String name, String information, String photo, int orden, boolean credential) {
        this.id = id;
        this.name = name;
        this.information = information;
        this.photo = photo;
        this.orden = orden;
        this.credential = credential;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", information='" + information + '\'' +
                ", photo='" + photo + '\'' +
                ", orden=" + orden +
                ", credential=" + credential +
                '}';
    }
}
