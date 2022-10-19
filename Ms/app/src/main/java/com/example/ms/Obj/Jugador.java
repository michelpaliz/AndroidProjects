package com.example.ms.Obj;

import androidx.annotation.NonNull;

import com.example.ms.Conf.Conf;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;

public class Jugador {

    private String nombre;
    private List<String> retos;

    public Jugador(){
        this.nombre = null;
        this.retos = new ArrayList<>(Conf.MAX_RETOS);
    }

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.retos = new ArrayList<>(Conf.MAX_RETOS);
    }

    public String getNombre() {
        return nombre;
    }

    public List<String> getRetos() {
        return retos;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRetos(List<String> retos) {
        this.retos = retos;
    }

    @NonNull
    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + nombre + '\'' +
                ", retos=" + retos +
                '}';
    }
}
