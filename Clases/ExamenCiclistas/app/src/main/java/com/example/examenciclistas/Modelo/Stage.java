package com.example.examenciclistas.Modelo;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Stage implements Serializable {
    private final String startCity;
    private final String finishCity;
    private final double distance;
    //Matriz de resultados
    private List<Result> results;

    //Datos de la competicion
    //Almacenamos la carrera
    public Stage(String startCity, String finishCity, double distance, List<Result> results) {
        this.startCity = startCity;
        this.finishCity = finishCity;
        this.distance = distance;
        this.results = results;
    }

    public String getStartCity() {
        return startCity;
    }

    public String getFinishCity() {
        return finishCity;
    }

    public double getDistance() {
        return distance;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }



    @NonNull
    @Override
    public String toString() {
        return "Stage{" +
                "startCity='" + startCity + '\'' +
                ", finishCity='" + finishCity + '\'' +
                ", distance=" + distance +
                ", results=" + results +
                '}';
    }
}
