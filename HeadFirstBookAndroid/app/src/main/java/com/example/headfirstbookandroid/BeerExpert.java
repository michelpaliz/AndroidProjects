package com.example.headfirstbookandroid;

import java.util.ArrayList;
import java.util.List;

public class BeerExpert {
        //Esta clase decide cual cerveza recomendar al usuario.


    public BeerExpert() {
    }

    /**
     *
     * @param color que el usuario seleccione
     * @return la marcas que estan disponible para este tipo o color de cerveza.
     */
    public List<String> getMarcas(String color){
        List<String> marcas = new ArrayList<>();
        if (color.equals("amber")){
            marcas.add("Jack Amber");
            marcas.add("Red Moose");
        }else{
            marcas.add("Jail Pale Ale");
            marcas.add("Gout Stout");
        }
        return  marcas;
    }


}
