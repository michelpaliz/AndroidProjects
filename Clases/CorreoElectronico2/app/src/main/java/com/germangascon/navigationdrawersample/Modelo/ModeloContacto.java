package com.germangascon.navigationdrawersample.Modelo;

import java.util.ArrayList;
import java.util.List;

public class ModeloContacto {
    private Contacto contacto;
    private List<Cuenta> cuentaList = new ArrayList<>();
    private List<Email> emails   = new ArrayList<>();

    @Override
    public String toString() {
        return "ModeloContacto{" +
                "contacto=" + contacto +
                ", cuentaList=" + cuentaList +
                ", emails=" + emails +
                '}';
    }
}
