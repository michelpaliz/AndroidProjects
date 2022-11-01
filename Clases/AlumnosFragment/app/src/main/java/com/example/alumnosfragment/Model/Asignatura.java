package com.example.alumnosfragment.Model;

import java.io.Serializable;

public class Asignatura implements Serializable {
    private final String codigoAsignatura;
    private final String nombreAsignatura;

    public Asignatura(String codigoAsignatura, String nombreAsignatura) {
        this.codigoAsignatura = codigoAsignatura;
        this.nombreAsignatura = nombreAsignatura;
    }

    public String getCodigoAsignatura() {
        return codigoAsignatura;
    }

    public String getNombreAsignatura() {
        return nombreAsignatura;
    }
}
