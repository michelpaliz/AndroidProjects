package com.example.basedatos.modelos;

import com.example.basedatos.ui.HashGenerator;

import java.security.NoSuchAlgorithmException;

public class User {

    private final int id;
    private String userName;
    private String password;

    public User(int id, String name, String password) throws NoSuchAlgorithmException {
        this.id = id;
        this.userName = name;
        setPassword(password);
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = HashGenerator.getSMAString.generator();
    }

}
