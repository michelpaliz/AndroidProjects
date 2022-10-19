package com.example.gridpais.Model;

public class Pais {

    private String imagen;
    private final String nombre;
    private final String capital;
    private final long poblacion;
    private final String isoAlpha;

    public Pais() {
        this.imagen = null;
        this.nombre = null;
        this.capital = null;
        this.poblacion = 0;
        this.isoAlpha = null;
    }
    public Pais(String imagen, String nombre, String capital, long poblacion, String isoAlpha) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.capital = capital;
        this.poblacion = poblacion;
        this.isoAlpha = isoAlpha;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getIsoAlpha() {
        return isoAlpha;
    }

    public String getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCapital() {
        return capital;
    }

    public long getPoblacion() {
        return poblacion;
    }


    @Override
    public String toString() {
        return "Pais{" +
                "imagen='" + imagen + '\'' +
                ", nombre='" + nombre + '\'' +
                ", capital='" + capital + '\'' +
                ", poblacion=" + poblacion +
                ", isoAlpha='" + isoAlpha + '\'' +
                '}';
    }
}
