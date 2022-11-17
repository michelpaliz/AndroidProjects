package com.example.examenciclistas.Modelo;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Comparator;

public class Cyclist implements Serializable {
    private final int cyclistId;
    private final String name;
    private final String surname;
    private final String team;
    private String tiempo;



    public Cyclist(int cyclistId, String name, String surname, String team) {
        this.cyclistId = cyclistId;
        this.name = name;
        this.surname = surname;
        this.team = team;
    }

    public int getCyclistId() {
        return cyclistId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getTeam() {
        return team;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getTiempo() {
        return tiempo;
    }


    public static class OrdenarPorTiempo implements Comparator<Cyclist>{

        /**
         *
         * @param cyclist1 primer obj
         * @param cyclist2 segundo obj
         * @return la ordenancia
         */
        @Override
        public int compare(Cyclist cyclist1 , Cyclist cyclist2 ) {
            return cyclist1.getTiempo().compareTo(cyclist2.getTiempo());
        }



    }

    @NonNull
    @Override
    public String toString() {
        return "Cyclist{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", team=" + team +
                '}';
    }



}
