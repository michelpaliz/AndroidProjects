package com.example.alumnosfragment.Contoller;

import android.content.Context;

import com.example.alumnosfragment.Model.Asignatura;
import com.example.alumnosfragment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ParserAsignatura {

    private List<Asignatura> asignaturas;
    private final  InputStream asignaturaFichero;


    public ParserAsignatura(Context contexto) {
        this.asignaturaFichero = contexto.getResources().openRawResource(R.raw.asignaturas);
    }

    public boolean startParser() {
        boolean parseado = false;
        String json;
        asignaturas = new ArrayList<>();

        try {
            int size = asignaturaFichero.available();
            byte[] buffer = new byte[size];
            asignaturaFichero.read(buffer);
            asignaturaFichero.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            JSONTokener jsonTokener = new JSONTokener(json);
            JSONArray jsonArray = new JSONArray(jsonTokener);
            for (int i = 0; i < jsonArray.length() ; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String codAsig = jsonObject.getString("codAsig");
                String nomASig = jsonObject.getString("nomAsig");
                asignaturas.add(new Asignatura(codAsig,nomASig));
            }
            parseado = true;


        }catch (IOException | JSONException io){
            io.printStackTrace();
            //Si salta la excepcion seria false como defecto que hemos puesto.
        }

        return parseado;

    }

    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }
}
