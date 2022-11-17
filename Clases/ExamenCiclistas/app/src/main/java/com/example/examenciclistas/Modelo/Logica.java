package com.example.examenciclistas.Modelo;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Logica {

    private List<Stage> stages;
    private List<Cyclist> cyclists;
    private Cyclist cyclist;
    private final Competition competition;
    private List<String> listFormatos;


    public Logica(Competition competicion) {
        this.competition = competicion;
        this.cyclists = Arrays.asList(this.competition.getCyclists());
        this.stages = Arrays.asList(this.competition.getStages());
        relacion();
        ordenarPorTiempo();
    }


    public void relacion() {

        for (int i = 0; i < cyclists.size(); i++) {
            listFormatos = new ArrayList<>();
            for (int j = 0; j < stages.size(); j++) {
                if (cyclists.get(i).getCyclistId() == stages.get(j).getResults().get(i).getCyclistId()) {
                    listFormatos.add(stages.get(j).getResults().get(i).getTiempoFormateado());
                    cyclists.get(i).setTiempo(sumarFormatos(listFormatos));
                }
            }
        }
    }


    public void ordenarPorTiempo(){
        List<Cyclist> cyclists = new ArrayList<>();
        cyclists = Arrays.asList(competition.getCyclists());
        Collections.sort(cyclists, new Cyclist.OrdenarPorTiempo());
    }



    public String sumarFormatos(List<String> listFormatos) {
        long tm = 0;
        for (String tmp : listFormatos) {
            String[] arr = tmp.split(":");
            tm += Integer.parseInt(arr[2]);
            tm += 60L * Integer.parseInt(arr[1]);
            tm += 3600L * Integer.parseInt(arr[0]);
        }

        long hh = tm / 3600;
        tm %= 3600;
        long mm = tm / 60;
        tm %= 60;
        long ss = tm;
        String total;
        return total = (format(hh) + ":" + format(mm) + ":" + format(ss));
    }




    private static String format(long dato){
        if (dato < 10) return "0" + dato;
        else return "" + dato;
    }



}
