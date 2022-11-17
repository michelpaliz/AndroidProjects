package com.example.tiendacoches.Model;


public class Tienda {
    //En la tienda tenemos que vender leches, antes de venderlo ...
    //Comprobar que la leche no este caducada
    //Comprobar que el producto de la leche tenga stock
    //(EN EL FRAGMENTO) comprobar que el usario introduzca un numero de stock correcto.

    private Leche lecheRecogida;
    private String mensaje;
    private int cantidadEscogida;
    private boolean ventaRealizada;

    public Tienda() {
        ventaRealizada = false;
    }


    public String getMensaje() {
        return mensaje;
    }

    private void mensajeError(Leche leche, int cantidad) {
        if (leche.getIsCaducado().equalsIgnoreCase("El producto esta caducado")) {
            mensaje = leche.getIsCaducado();
        } else if (leche.getNumeroStock() < cantidad) {
            mensaje = mensaje + " ,El producto no tiene suficente stock";
        }
    }

    public void resumenCompra(){
        if (ventaRealizada){
            System.out.println("esta es la cantidad " + cantidadEscogida + " esto es el precio " + lecheRecogida.getPrecioUnidad());
            float importe = cantidadEscogida * lecheRecogida.getPrecioVenta();
            mensaje = "Total de la compra es " + importe;
        }

    }


    public boolean ventaLeche(Leche leche, int cantidad) {
        cantidadEscogida = cantidad;
        lecheRecogida = leche;
        if (leche.getNumeroStock() >= cantidad && leche.getIsCaducado().equalsIgnoreCase("el producto no esta caducado")) {
            lecheRecogida.setNumeroStock(leche.getNumeroStock() - cantidad);
            mensaje = "La eleccion se ha realizado con exito ";
            ventaRealizada = true;
            return true;
        } else {
            mensajeError(lecheRecogida, cantidad);
        }


        return false;
    }




}
