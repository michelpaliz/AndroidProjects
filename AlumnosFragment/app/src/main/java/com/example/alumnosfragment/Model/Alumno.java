package com.example.alumnosfragment.Model;

import java.io.Serializable;
import java.util.List;

public class Alumno implements Serializable {

    private final String nia;
    private final String nombre;
    private final String apellido;
    private final String apellido2;
    private final String nacimiento;
    private final String email;
    private final List<Nota> notas;

    public Alumno(String nia, String nombre, String apellido, String apellido2, String nacimiento, String email, List<Nota> notas) {
        this.nia = nia;
        this.nombre = nombre;
        this.apellido = apellido;
        this.apellido2 = apellido2;
        this.nacimiento = nacimiento;
        this.email = email;
        this.notas = notas;
    }

    public String getNia() {
        return nia;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getApellido2() {
        return apellido2;
    }

    public String getNacimiento() {
        return nacimiento;
    }

    public String getEmail() {
        return email;
    }

    public List<Nota> getNotas() {
        return notas;
    }

}