package com.example.alumnosfragment.Contoller;

import android.content.Context;
import android.util.Log;

import com.example.alumnosfragment.Model.Alumno;
import com.example.alumnosfragment.Model.Nota;
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

public class ParserAlumno {

    private List<Alumno> alumnos;
    private final InputStream alumnoFichero;

    public ParserAlumno(Context contexto) {
        this.alumnoFichero = contexto.getResources().openRawResource(R.raw.alumnos_notas);
    }


    public boolean startParser()  {
        boolean parseado = false;
        String json = null;
        alumnos = new ArrayList<>();
        try{
            int size = alumnoFichero.available();
            byte[] buffer = new byte[size];
            alumnoFichero.read(buffer);
            alumnoFichero.close();
            Alumno alumno;
            Nota nota;
            json = new String(buffer, StandardCharsets.UTF_8);
            JSONTokener jsonTokener = new JSONTokener(json);
            JSONArray jsonArray = new JSONArray(jsonTokener);
            JSONArray jsonNotas = null;
            List<Nota>notas = new ArrayList<>();
            List<Nota>notasFinal ;

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nia = jsonObject.getString("nia");
                String nombre = jsonObject.getString("nombre");
                String apellido1 = jsonObject.getString("apellido1");
                String apellido2 = jsonObject.getString("apellido2");
                String fechaNacimiento = jsonObject.getString("fechaNacimiento");
                String email = jsonObject.getString("email");
                //Creamos el array para las notas
                 jsonNotas = jsonObject.getJSONArray("notas");
                for (int j = 0; j < jsonNotas.length(); j++) {
                    jsonObject = jsonNotas.getJSONObject(j);
                    notas.add(new Nota(jsonObject.getString("calificacion"),jsonObject.getString("codAsig")));
                }

                alumnos.add(new Alumno(nia, nombre, apellido1, apellido2, fechaNacimiento, email, notas));
                parseado = true;
            }

        }catch (IOException | JSONException io){
            io.printStackTrace();
        }

        return  parseado;

    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }
}
