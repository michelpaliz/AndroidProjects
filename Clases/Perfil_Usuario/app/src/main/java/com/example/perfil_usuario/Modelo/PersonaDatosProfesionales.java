package com.example.perfil_usuario.Modelo;

public class PersonaDatosProfesionales {

    private static int contador = 0;

    private long id;
    private final String nombreEmpresa;
    private final String cif;
    private final String email;
    private final String paginaWeb;
    private final String direccion;


    public PersonaDatosProfesionales(String nombreEmpresa, String cif, String email, String paginaWeb, String direccion) {
        this.id = contador++;
        this.nombreEmpresa = nombreEmpresa;
        this.cif = cif;
        this.email = email;
        this.paginaWeb = paginaWeb;
        this.direccion = direccion;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public String getCif() {
        return cif;
    }

    public String getEmail() {
        return email;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public String getDireccion() {
        return direccion;
    }
}
