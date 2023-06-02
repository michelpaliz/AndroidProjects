package com.example.caminoalba.models;

import java.io.Serializable;

public class Badge implements Serializable {
    String name;
    String date;

    public Badge() {
    }

    public Badge(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Badge{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
