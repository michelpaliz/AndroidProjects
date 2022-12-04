package com.germangascon.navigationdrawersample.Modelo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Email implements Serializable {

    @SerializedName("from")
    private final  String correoOrigen;
    @SerializedName("to")
    private final String correoDestino;
    @SerializedName("subject")
    private final String tema;
    @SerializedName("body")
    private final String texto;
    @SerializedName("sentOn")
    private final String fecha;
    @SerializedName("read")
    private final boolean leido;
    @SerializedName("deleted")
    private final boolean eliminado;
    @SerializedName("spam")
    private final boolean spam;

    public Email(String corregoOrigen, String correoDestino, String tema, String texto, String fecha, boolean leido, boolean eliminado, boolean spam) {
        this.correoOrigen = corregoOrigen;
        this.correoDestino = correoDestino;
        this.tema = tema;
        this.texto = texto;
        this.fecha = formatearFecha(fecha);
        this.leido = leido;
        this.eliminado = eliminado;
        this.spam = spam;
    }


    public String getCorreoOrigen() {
        return correoOrigen;
    }

    public String getCorreoDestino() {
        return correoDestino;
    }

    public String getTema() {
        return tema;
    }

    public String getTexto() {
        return texto;
    }

    public String getFecha() {



        return fecha;
    }

    public boolean isLeido() {
        return leido;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public boolean isSpam() {
        return spam;
    }


    public String formatearFecha(String fecha)  {
        Date date = null;
       try {
           date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(fecha);
       }catch (Exception e){
           e.printStackTrace();
       }

        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(" yyyy-MM-dd HH:mm");
        assert date != null;
        return simpleDateFormat2.format(date);
    }

    //Hacemos algunas comparaciones que nos sirven para ordenar.

    public static class ComparadorTiempo implements Comparator<Email>{

        @Override
        public int compare(Email email1, Email email2) {
            return email2.getFecha().compareTo(email1.getFecha());
        }
    }


    @Override
    public String toString() {
        return "Email{" +
                "correoOrigen='" + correoOrigen + '\'' +
                ", correoDestino='" + correoDestino + '\'' +
                ", tema='" + tema + '\'' +
                ", texto='" + texto + '\'' +
                ", fecha='" + fecha + '\'' +
                ", leido=" + leido +
                ", eliminado=" + eliminado +
                ", spam=" + spam +
                '}';
    }
}
