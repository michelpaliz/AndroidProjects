package com.example.examplefragment.Controller;

import android.content.Context;
import android.util.JsonToken;
import android.util.Log;

import com.example.examplefragment.Model.Contacto;
import com.example.examplefragment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private List<Contacto> contactos;
    private final InputStream contactoFichero;

//    Al constructor le pasamos el contexto para que pueda tener acceso a los recursos
    public Parser(Context contexto) {
        this.contactoFichero = contexto.getResources().openRawResource(R.raw.contacts);
    }

    public boolean startParser(){
        boolean parseado = false;
        String json = null;
        contactos = new ArrayList<>();
        Contacto contacto ;
        try{
            int size = contactoFichero.available();
            byte[] buffer = new byte[size];
            contactoFichero.read(buffer);
            contactoFichero.close();
            json = new String(buffer, "UTF-8");
            JSONTokener jsonTokener = new JSONTokener(json);
            JSONArray jsonArray = new JSONArray(jsonTokener);
            /* Inicializamos el array con el tamaño del array en el JSON */
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String nombre = jsonObject.getString("name");
                String apellido1 = jsonObject.getString("firstSurname");
                String apellido2 = jsonObject.getString("secondSurname");
                String photo = jsonObject.getString("photo");
                String nacimiento =jsonObject.getString("birth");
                String empresa = jsonObject.getString("company");
                String email = jsonObject.getString("email");
                String telefono1 = jsonObject.getString("phone1");
                String telefono2 = jsonObject.getString("phone2");
                String direccion = jsonObject.getString("address");
                contacto = new Contacto(id, nombre, apellido1, apellido2, photo, nacimiento, empresa, email, telefono1, telefono2, direccion );
                contactos.add(contacto);
            }
            Log.d("parser", String.valueOf(contactos.size()));
            parseado = true;

        }catch (IOException | JSONException io){
            io.printStackTrace();
            parseado = false;
        }
        return parseado;
    }


    public List<Contacto> getContactos() {
        return contactos;
    }
}
