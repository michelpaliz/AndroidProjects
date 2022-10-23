package com.example.alumnosfragment.Model;

import org.w3c.dom.CDATASection;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Alumno implements Serializable {

    private final String nia;
    private final String nombre;
    private final String apellido;
    private final String apellido2;
    private final String nacimiento;
    private final String email;
    private final List<Nota> notas;
    private int edad;

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

    public int calcularEdad(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        int edadPredeterminado =0;
        Period p = null;
        try {
            date = sdf.parse(nacimiento);
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            assert date != null;
            gregorianCalendar.setTime(date);
            LocalDate hoy = LocalDate.now();
            LocalDate nacimiento = gregorianCalendar.toZonedDateTime().toLocalDate();
            p = Period.between(nacimiento,hoy);
        }catch (ParseException io){
            io.printStackTrace();
        }
        if (p == null){
            return edadPredeterminado;
        }
        return  p.getYears();
    }

    public int getEdad() {
        return calcularEdad();
    }


    @Override
    public String toString() {
        return "Alumno{" +
                "nia='" + nia + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", apellido2='" + apellido2 + '\'' +
                ", nacimiento='" + nacimiento + '\'' +
                ", email='" + email + '\'' +
                ", notas=" + notas +
                ", edad=" + edad +
                '}';
    }
}