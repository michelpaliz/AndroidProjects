package com.example.startbuzz.Model;

import java.io.Serializable;

public class Coffe implements Serializable {
    private final String nombre;
    private final int precio;
    private final String codCoffe;
    private final String descripcionCorta;
    private final String descripcionLarga;
    private final int img;

    public Coffe(String nombre, int precio, String codCoffe, String descripcion, String descripcionLarga, int img) {
        this.nombre = nombre;
        this.precio = precio;
        this.codCoffe = codCoffe;
        this.descripcionCorta = descripcion;
        this.descripcionLarga = descripcionLarga;
        this.img = img;
    }

    public String getCodCoffe() {
        return codCoffe;
    }

    public String getDescripcionLarga() {
        return descripcionLarga;
    }

    public int getPrecio() {
        return precio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcionCorta ;
    }

    public int getImg() {
        return img;
    }
}
