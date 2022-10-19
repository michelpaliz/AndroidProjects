package com.example.fragment.Model;

public class Letter {

    private String de;
    private String detalle;
    private String dato;

    public Letter(String de, String detalle, String dato) {
        this.de = de;
        this.detalle = detalle;
        this.dato = dato;
    }

    public String getDe() {
        return de;
    }

    public String getDetalle() {
        return detalle;
    }

    public String getDato() {
        return dato;
    }
}
