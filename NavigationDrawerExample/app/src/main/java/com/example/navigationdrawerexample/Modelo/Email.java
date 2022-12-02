package com.example.navigationdrawerexample.Modelo;

import java.util.List;

public class Email {

    private final int id;
    private final String nombre;
    private final String apellido;
    private final String email;
    private List<Contacto> contactoList;

    public Email(int id, String nombre, String apellido, String email, List<Contacto>contactoList) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contactoList = contactoList;
    }

    public int getId() {
        return id;
    }

    public List<Contacto> getContactoList() {
        return contactoList;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }
}
