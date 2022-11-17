package com.example.alumnosfragment.Model;

import java.io.Serializable;

public class Nota implements Serializable {
    private String calificacion;
    private String codAsig;

    public Nota(String calificacion, String codAsig) {
        this.calificacion = calificacion;
        this.codAsig = codAsig;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public String getCodAsig() {
        return codAsig;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "calificacion='" + calificacion + '\'' +
                ", codAsig='" + codAsig + '\'' +
                '}';
    }
}
