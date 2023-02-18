package com.example.basedatos.modelos;

public class User {

    private final int id;
    private String userName;
    private String password;

    public User(int id, String name, String password) {
        this.id = id;
        this.userName = name;
        this.password = password;
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
        this.password = password;
    }
}
