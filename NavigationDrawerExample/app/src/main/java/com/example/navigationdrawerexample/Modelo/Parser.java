package com.example.navigationdrawerexample.Modelo;

import android.content.Context;
import android.util.Log;

import com.example.navigationdrawerexample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<Email> emails;
    private final InputStream emailsFichero;

    //    Al constructor le pasamos el contexto para que pueda tener acceso a los recursos
    public Parser(Context contexto) {
        this.emailsFichero = contexto.getResources().openRawResource(R.raw.correos);
    }

    public boolean startParser(){
        String json;
        emails = new ArrayList<>();
        try{
            int size = emailsFichero.available();
            byte[] buffer = new byte[size];
            emailsFichero.read(buffer);
            emailsFichero.close();
            json = new String(buffer, "UTF-8");
            JSONTokener jsonTokener = new JSONTokener(json);
            JSONArray jsonArray = new JSONArray(jsonTokener);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String nombrePropio = jsonObject.getString("name");
                String apellidoPropio = jsonObject.getString("firstSurname");
                String emailPropio = jsonObject.getString("email");

                List<Contacto> contactos = new ArrayList<>();
                for (int j = 0; j < jsonArray.length(); j++) {
                    jsonObject = jsonArray.getJSONObject(j);
                    id = jsonObject.getInt("id");
                    String nombre = jsonObject.getString("name");
                    String apellido1 = jsonObject.getString("firstSurname");
                    String apellido2 = jsonObject.getString("secondSurname");
                    String photo = jsonObject.getString("photo");
                    String nacimiento =jsonObject.getString("birth");
                    String emailStr = jsonObject.getString("email");
                    String telefono1 = jsonObject.getString("phone1");
                    String telefono2 = jsonObject.getString("phone2");
                    String direccion = jsonObject.getString("address");
                    contactos.add(new Contacto(id, nombre, apellido1, apellido2, photo, nacimiento,  emailStr, telefono1, telefono2, direccion ));
                }
                emails.add(new Email(id,nombrePropio,apellidoPropio,emailPropio,contactos));


            }
            Log.d("parser", String.valueOf(emails.size()));

        }catch (JSONException | IOException io){
            io.printStackTrace();
            return false;
        }

        return  true;
    }


    public List<Email> getEmails() {
        return emails;
    }









}
