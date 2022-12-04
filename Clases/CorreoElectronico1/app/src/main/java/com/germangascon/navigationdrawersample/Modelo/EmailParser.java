package com.germangascon.navigationdrawersample.Modelo;

import android.content.Context;
import android.util.Log;

import com.germangascon.navigationdrawersample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EmailParser {
    private Cuenta cuenta;
    private ModeloContacto modeloContacto;
    private final InputStream emailsFichero;

    //    Al constructor le pasamos el contexto para que pueda tener acceso a los recursos
    public EmailParser(Context contexto) {
        this.emailsFichero = contexto.getResources().openRawResource(R.raw.correos);
    }


//    public boolean startParser1() throws IOException {
//
//
//        String json;
//        int size = emailsFichero.available();
//        byte[] buffer = new byte[size];
//        emailsFichero.read(buffer);
//        emailsFichero.close();
//        Gson gson = new Gson();
//        json = new String(buffer, "UTF-8");
//        modeloContacto = gson.fromJson(json, ModeloContacto.class);
//        System.out.println("mensaje " + modeloContacto);
//        return modeloContacto != null;
//    }

    public boolean startParser() {
        try {
            int size = emailsFichero.available();
            byte[] buffer = new byte[size];
            emailsFichero.read(buffer);
            emailsFichero.close();
            String json = new String(buffer, "UTF-8");
            JSONTokener jsonTokener = new JSONTokener(json);
            JSONArray jsonArray = new JSONArray(jsonTokener);
            cuenta = null;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject jsonAccount = jsonObject.getJSONObject("myAccount");
                int idPropio = jsonAccount.getInt("id");
                String nombrePropio = jsonAccount.getString("name");
                String apellidoPropio = jsonAccount.getString("firstSurname");
                String emailPropio = jsonAccount.getString("email");

                JSONArray arrayContacts = jsonObject.getJSONArray("contacts");
                List<Contacto> contactos = new ArrayList<>();


                for (int j = 0; j < arrayContacts.length(); j++) {
                    JSONObject jsonObject1 = arrayContacts.getJSONObject(j);
                    int id = jsonObject1.getInt("id");
                    String nombre = jsonObject1.getString("name");
                    String apellido1 = jsonObject1.getString("firstSurname");
                    String apellido2 = jsonObject1.getString("secondSurname");
                    int photo = jsonObject1.getInt("foto");
                    String nacimiento = jsonObject1.getString("birth");
                    String emailStr = jsonObject1.getString("email");
                    String telefono1 = jsonObject1.getString("phone1");
                    String telefono2 = jsonObject1.getString("phone2");
                    String direccion = jsonObject1.getString("address");
                    contactos.add(new Contacto(id, nombre, apellido1, apellido2, nacimiento,photo, emailStr, telefono1, telefono2, direccion));

                }

                JSONArray arrayMails = jsonObject.getJSONArray("mails");
                List<Email> emails = new ArrayList<>();
                for (int j = 0; j < arrayMails.length(); j++) {
                     JSONObject jsonObject2 = arrayMails.getJSONObject(j);
                    String correoOrigen = jsonObject2.getString("from");
                    String correoDestino = jsonObject2.getString("to");
                    String tema = jsonObject2.getString("subject");
                    String texto = jsonObject2.getString("body");
                    String fechaEnvio = jsonObject2.getString("sentOn");
                    boolean leido = jsonObject2.getBoolean("readed");
                    boolean eliminado = jsonObject2.getBoolean("deleted");
                    boolean spam = jsonObject2.getBoolean("spam");
                    emails.add(new Email(correoOrigen, correoDestino, tema, texto, fechaEnvio, leido, eliminado, spam));
                }
                cuenta = (new Cuenta(idPropio, nombrePropio, apellidoPropio, emailPropio, contactos, emails));
            }
            Log.d("parser", cuenta.toString());
            return true;


        } catch (Exception io ) {
            io.printStackTrace();
            return false;
        }

    }


    public Cuenta getCuenta() {
        return cuenta;
    }

    public ModeloContacto getModeloContacto() {
        return modeloContacto;
    }
}
