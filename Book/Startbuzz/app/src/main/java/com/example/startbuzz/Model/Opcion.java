package com.example.startbuzz.Model;

public class Opcion {
    private final String titulo;
    private final String descripcion;

    public Opcion(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTitulo() {
        return titulo;
    }
}
