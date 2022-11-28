package com.example.perfil_usuario.Modelo;

public class Usuario {

    private final String nombre;
    private String contrasenya;
    private final Persona persona;

    public Usuario(String nombre, String contrasenya, Persona persona) {
        this.nombre = nombre;
        this.contrasenya = contrasenya;
        this.persona = persona;
    }

    public Persona getPersona() {
        return persona;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }
}
