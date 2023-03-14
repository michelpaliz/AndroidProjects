package com.example.caminoalba.rest;

import com.example.caminoalba.config.Config;
import com.example.caminoalba.interfaces.IAPIservice;

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
