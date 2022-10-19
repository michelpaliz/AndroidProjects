package com.example.selectcountry.Model;

public class Pais {

    private String imagen;
    private String nombre;
    private String capital;
    private String poblacion;
    private String isoAlpha;

    public Pais() {
        this.imagen = null;
        this.nombre = null;
        this.capital = null;
        this.poblacion = null;
        this.isoAlpha = null;
    }
    public Pais(String imagen, String nombre, String capital, String poblacion, String isoAlpha) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.capital = capital;
        this.poblacion = poblacion;
        this.isoAlpha = isoAlpha;
    }

    public String getIsoAlpha() {
        return isoAlpha;
    }

    public void setIsoAlpha(String isoAlpha) {
        this.isoAlpha = isoAlpha;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
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
