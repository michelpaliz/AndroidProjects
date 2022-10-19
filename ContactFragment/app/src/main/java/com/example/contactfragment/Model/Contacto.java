package com.example.contactfragment.Model;

import androidx.recyclerview.widget.RecyclerView;

public class Contacto {
    private final String nombre;
    private final String apellidos;
    private final String nacimiento;
    private final String direccion;
    private final String empresa;
    private final String telefono1;
    private final String telefono2;


    public Contacto(String nombre, String apellidos, String nacimiento, String direccion, String empresa, String telefono1, String telefono2) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nacimiento = nacimiento;
        this.direccion = direccion;
        this.empresa = empresa;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
    }

    public String getNacimiento() {
        return nacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
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
