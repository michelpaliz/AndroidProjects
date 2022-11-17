package com.example.tiendacoches.Vista.Parser;

import android.content.Context;
import android.util.Log;

import com.example.tiendacoches.Model.Leche;
import com.example.tiendacoches.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    //Necesitamos cargar nuestros datos en nuestros objetos

    private List<Leche> listaLeches;
    private final InputStream inputStream;

    public Parser(Context context) {
        this.inputStream  =  context.getResources().openRawResource(R.raw.leches);

    }

    public boolean startParser()  {
        boolean parseado = false;
        listaLeches = new ArrayList<>();

        if (inputStream==null){
            throw new NullPointerException();
        }

        try{
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONTokener jsonTokener = new JSONTokener(json);
            JSONArray jsonArrayLeches = new JSONArray(jsonTokener);
            for (int i = 0; i < jsonArrayLeches.length() ; i++) {
                JSONObject jsonObject = jsonArrayLeches.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String modelo = jsonObject.getString("modelo");
                String fechaProduccion = jsonObject.getString("fechaProduccion");
                String fechaCompraStock = jsonObject.getString("fechaCompraStock");
                String fechaCaducidad = jsonObject.getString("fechaCaducidad");
                float precioUnidadCompra = Float.parseFloat(String.valueOf(jsonObject.getDouble("precioUnidadCompra")));
                float precioUnidadVenta = Float.parseFloat(String.valueOf(jsonObject.getDouble("precioUnidadVenta")));
                int stock = jsonObject.getInt("stock");
                Leche leche = new Leche(id,modelo,fechaProduccion,fechaCompraStock,fechaCaducidad,precioUnidadCompra,precioUnidadVenta,stock);
                listaLeches.add(leche);
            }
            System.out.println(listaLeches.size());
            parseado = true;

        }catch (IOException | JSONException io){
            io.printStackTrace();
        }

        Log.d("listaProductos", "startParser:  " + listaLeches.toString());
        return parseado;

    }

    public List<Leche> getListaLeches() {
        return listaLeches;
    }
}
