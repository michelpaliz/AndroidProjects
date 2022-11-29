package com.example.perfil_usuario.Modelo;

public class Persona {

    private static int contador = 0;

    private final long id;
    private final String dni;
    private final  String nombre;
    private final String apellido;
    private final String fechaNacimiento;
    private final String direccion;
    private final PersonaDatosProfesionales datosProfesionales;

    public Persona(String dni, String nombre, String apellido, String fechaNacimiento, String direccion, PersonaDatosProfesionales datosProfesionales) {
        this.id = contador++;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.datosProfesionales = datosProfesionales;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public PersonaDatosProfesionales getDatosProfesionales() {
        return datosProfesionales;
    }
}
