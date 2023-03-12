package com.example.caminoalba.rest;

import android.widget.Toast;

import com.example.caminoalba.Config.Config;
import com.example.caminoalba.interfaces.IAPIservice;
import com.example.caminoalba.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static IAPIservice instance;

    public static synchronized IAPIservice getInstance() {
        if(instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.CLIENT_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            instance = retrofit.create(IAPIservice.class);
        }
        return instance;
    }




}
