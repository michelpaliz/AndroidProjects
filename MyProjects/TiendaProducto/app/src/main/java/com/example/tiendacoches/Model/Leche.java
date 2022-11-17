package com.example.tiendacoches.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;

public class Leche implements Serializable{

    private final int id;
    private final String marca;
    private final String fechaProduccion;
    private final String fechaCompraStock;
    private final String fechaCaducidad;
    private final float precioUnidad;
    private final float precioVenta;
    private  int numeroStock;

    //PARA SABER SI ESTA CADUCADO O NO
    private boolean caducado;


    public Leche(int id, String marca, String fechaProduccion, String fechaCompraStock, String fechaCaducidad, float precioUnidad, float precioVenta, int numeroStock) {
        this.id = id;
        this.marca = marca;
        this.fechaProduccion = fechaProduccion;
        this.fechaCompraStock = fechaCompraStock;
        this.fechaCaducidad = fechaCaducidad;
        this.precioUnidad = precioUnidad;
        this.precioVenta = precioVenta;
        this.numeroStock = numeroStock;

        caducado = comprobarCaducidad();
    }

    public void setNumeroStock(int numeroStock) {
        this.numeroStock = numeroStock;
    }

    public int getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public String getFechaProduccion() {
        return fechaProduccion;
    }

    public String getFechaCompraStock() {
        return fechaCompraStock;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public float getPrecioUnidad() {
        return precioUnidad;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public int getNumeroStock() {
        return numeroStock;
    }

    public boolean isCaducado() {
        return caducado;
    }

    public String getIsCaducado(){
        if (caducado){
            return "El producto esta caducado";
        }
        return  "El producto no esta caducado";
    }

    private boolean comprobarCaducidad()  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date fechaCaducidad =  sdf.parse(this.fechaCaducidad);
            LocalDate hoy = LocalDate.now();
            GregorianCalendar gregorianCalendarCaducidad= new GregorianCalendar();
            assert fechaCaducidad != null;
            gregorianCalendarCaducidad.setTime(fechaCaducidad);
            LocalDate  localDateCaducidad =  gregorianCalendarCaducidad.toZonedDateTime().toLocalDate();
            if (localDateCaducidad.isBefore(hoy)){
                return true;
            }

        }catch ( ParseException io){
            io.printStackTrace();
        }
        return false;
    }

    public String redondear(float numero){
       return String.format("%2.2f", numero);
    }


    @NonNull
    @Override
    public String toString() {
        return "Leche{" +
                "id=" + id +
                ", marca='" + marca + '\'' +
                ", fechaProduccion='" + fechaProduccion + '\'' +
                ", fechaCompraStock='" + fechaCompraStock + '\'' +
                ", fechaCaducidad='" + fechaCaducidad + '\'' +
                ", precioUnidad=" + redondear(precioUnidad) +
                ", precioVenta=" + redondear(precioVenta) +
                ", numeroStock=" + numeroStock +
                ", caducado=" + caducado +
                '}';
    }


}
