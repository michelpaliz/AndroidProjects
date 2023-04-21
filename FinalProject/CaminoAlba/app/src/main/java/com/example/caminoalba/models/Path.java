package com.example.caminoalba.models;

public class Path {

    private String id;
    private String name;
    private String information;
    private String photo;
    private boolean credential;

    public Path(String id, String name, String information, String photo, boolean credential) {
        this.id = id;
        this.name = name;
        this.information = information;
        this.photo = photo;
        this.credential = credential;
    }

    public boolean isCredential() {
        return credential;
    }

    public void setCredential(boolean credential) {
        this.credential = credential;
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


}
