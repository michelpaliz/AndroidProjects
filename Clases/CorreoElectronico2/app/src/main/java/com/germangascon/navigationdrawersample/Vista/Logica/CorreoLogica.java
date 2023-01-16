package com.germangascon.navigationdrawersample.Vista.Logica;

import com.germangascon.navigationdrawersample.Modelo.Contacto;
import com.germangascon.navigationdrawersample.Modelo.Cuenta;
import com.germangascon.navigationdrawersample.Modelo.Email;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class CorreoLogica {

    private HashMap<Email, Contacto> correosGestion;
    private final Cuenta cuenta;

    public CorreoLogica(Cuenta cuenta) {
        this.cuenta = cuenta;

    }

    public HashMap<Email, Contacto> cargarDatosCorreosEnviados() {
        correosGestion = new HashMap<>();
        for (int i = 0; i < cuenta.getContactoList().size(); i++) {
            for (int j = 0; j < cuenta.getCorreos().size(); j++) {
                if (cuenta.getContactoList().get(i).getEmail().equalsIgnoreCase(cuenta.getCorreos().get(j).getCorreoDestino()) && !cuenta.getCorreos().get(j).isEliminado()  && !cuenta.getCorreos().get(j).isSpam()) {
//                    correosParaComparar.add(cuenta.getCorreos().get(i)); //voy anyadiendo los correos para compararlo
                    correosGestion.put(cuenta.getCorreos().get(j), cuenta.getContactoList().get(i));
                }
            }
        }

        return  correosGestion;
    }


    public HashMap<Email, Contacto> cargarDatosCorreosRecibidos() {
        correosGestion = new HashMap<>();
        for (int i = 0; i < cuenta.getContactoList().size(); i++) {
            for (int j = 0; j < cuenta.getCorreos().size(); j++) {
                if (cuenta.getContactoList().get(i).getEmail().equalsIgnoreCase(cuenta.getCorreos().get(j).getCorreoOrigen()) && !cuenta.getCorreos().get(j).isEliminado()  && !cuenta.getCorreos().get(j).isSpam()) {
                    correosGestion.put(cuenta.getCorreos().get(j), cuenta.getContactoList().get(i));
                }
            }
        }
        return correosGestion ;
    }


    //Cargamos solo correos de spam ya que estos correos no estan registrados en nuestra lista de contactos.
    public HashMap<Email, Email>cargarDatosCorreosSpam() {
        HashMap<Email,Email>  correosGestion = new HashMap<>();
        List<Email> correosSpam = cuenta.getCorreos().stream().filter(Email::isSpam).collect(Collectors.toList());
        for (Email email: correosSpam) {
             correosGestion.put(email , email);
        }

        return correosGestion;
    }


    public HashMap<Email, Contacto>cargarDatosCorreosNoLeidos() {
        HashMap<Email,Contacto>  correosGestion = new HashMap<>();
        List<Email> correosNoLeidos = cuenta.getCorreos().stream().filter(email -> !email.isLeido() && !email.isSpam() && !email.isEliminado()).collect(Collectors.toList());
        for (int i = 0; i < cuenta.getContactoList().size(); i++) {
            for (int j = 0; j < correosNoLeidos.size(); j++) {
                if (cuenta.getContactoList().get(i).getEmail().equalsIgnoreCase(correosNoLeidos.get(j).getCorreoDestino())) {
                    correosGestion.put(correosNoLeidos.get(j), cuenta.getContactoList().get(i));
                }
            }
        }

        return correosGestion;
    }


    public HashMap<Email, Contacto>cargarDatosCorreosEliminados() {
        HashMap<Email, Contacto> correosGestion = new HashMap<>();
        List<Email> correosEliminados = cuenta.getCorreos().stream().filter(Email::isEliminado).collect(Collectors.toList());
        for (int i = 0; i < cuenta.getContactoList().size(); i++) {
            for (int j = 0; j < correosEliminados.size(); j++) {
                if (cuenta.getContactoList().get(i).getEmail().equalsIgnoreCase(correosEliminados.get(j).getCorreoOrigen()) && cuenta.getCorreos().get(j).isEliminado()) {
                    correosGestion.put(correosEliminados.get(j), cuenta.getContactoList().get(i));
                }
            }
        }
        return correosGestion;
    }

    public HashMap<Email, Contacto> getCorreosEliminados() {
        return cargarDatosCorreosEliminados();
    }


    public HashMap<Email, Contacto> getCorreosNoLeidos() {
        return cargarDatosCorreosNoLeidos();
    }


    public HashMap<Email, Email> getCorreosSpam() {
        return cargarDatosCorreosSpam();
    }


    public HashMap<Email, Contacto> getCorreosRecibidos() {
        return cargarDatosCorreosRecibidos();
    }



    public HashMap<Email, Contacto> getCorreosEnviados() {
        return cargarDatosCorreosEnviados();
    }
}
