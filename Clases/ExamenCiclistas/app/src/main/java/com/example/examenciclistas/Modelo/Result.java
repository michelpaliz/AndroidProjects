package com.example.examenciclistas.Modelo;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Result implements Serializable {
    private final int cyclistId;
    private final int hours;
    private final int minutes;
    private final int seconds;
    private String tiempoFormateado;

    //Un ciclista tiene su tiempo
    public Result(int cyclistId, int hours, int minutes, int seconds) {
        this.cyclistId = cyclistId;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        tiempoFormateado =  String.format("%s:%s:%s",format(hours),format(minutes),format(seconds));
    }

    public int getCyclistId() {
        return cyclistId;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public String getTiempoFormateado(){
        return tiempoFormateado;
    }

    private static String format(long dato){
        if (dato < 10) return "0" + dato;
        else return "" + dato;
    }

    @NonNull
    @Override
    public String toString() {
        return "Result{" +
                "cyclistId=" + cyclistId +
                ", hour=" + hours +
                ", minutes=" + minutes +
                ", seconds=" + seconds +
                '}';
    }
}
