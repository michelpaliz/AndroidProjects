package com.germangascon.navigationdrawersample.Modelo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Contacto implements Serializable {
    private final int id;
    private final String nombre;
    private final String primerApellido;
    private final String segundoApellido;
    private final String fechaNacimiento;
    private final int foto;
    private final String email;
    private final String telefono1;
    private final String telefono2;
    private final String direccion;

    public Contacto(int id, String nombre, String primerApellido,String segundoApellido, String fechaNacimiento, int foto, String email, String telefono1, String telefono2, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.fechaNacimiento = fechaNacimiento;
        this.foto = foto;
        this.email = email;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
        this.direccion = direccion;
    }


    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public int getFoto() {
        return foto;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public String getDireccion() {
        return direccion;
    }


    @Override
    public String toString() {
        return "Contacto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", primerApellido='" + primerApellido + '\'' +
                ", segundoApellido='" + segundoApellido + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", foto='" + foto + '\'' +
                ", email='" + email + '\'' +
                ", telefono1='" + telefono1 + '\'' +
                ", telefono2='" + telefono2 + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}
