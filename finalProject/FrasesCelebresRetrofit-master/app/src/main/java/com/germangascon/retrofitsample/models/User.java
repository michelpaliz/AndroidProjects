package com.germangascon.retrofitsample.models;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String lastName;
    private String email;
    private String type;
    private String passsword;

    public User() {
    }

    public User(String name, String lastName, String email, String password, String type ) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.passsword = password;
        this.type = type;

    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public String getPasssword() {
        return passsword;
    }

    public void setPasssword(String passsword) {
        this.passsword = passsword;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", type='" + type + '\'' +
                ", passsword='" + passsword + '\'' +
                '}';
    }
}
