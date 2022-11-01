package com.example.examplefragment.Model;

import java.io.Serializable;

public class Contacto implements Serializable {
    private final int id;
    private final String nombre;
    private final String apellido1;
    private final String apellido2;
    private final String photo;
    private final String nacimiento;
    private final String direccion;
    private final String empresa;
    private final String email;
    private final String telefono1;
    private final String telefono2;


    public Contacto(int id, String nombre, String apellido1, String apellido2, String photo, String empresa,String email, String telefono1, String telefono2,  String nacimiento, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.photo = photo;
        this.nacimiento = nacimiento;
        this.direccion = direccion;
        this.empresa = empresa;
        this.email = email;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
    }

    public int getId() {
        return id;
    }

    public String getNacimiento() {
        return nacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getApellido2() {return apellido2;}

    public String getPhoto() {
        return photo;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }
}
