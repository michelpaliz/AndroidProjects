package com.germangascon.retrofitsample.rest;

import com.germangascon.retrofitsample.interfaces.IAPIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static IAPIService instance;
    public static final int PORT = 9080;
    public static final String IP = "192.168.9.127";
    public static final String BASE_URL = "http://" + IP + ":" + PORT;

    public static String URL = "http://192.168.9.127:9080/";
    /* Lo hacemos privado para evitar que se puedan crear instancias de esta forma */
    private RestClient() {

    }

    public static String getURL() {
        return URL;
    }

    public static synchronized IAPIService getInstance() {
        if(instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            instance = retrofit.create(IAPIService.class);
        }
        return instance;
    }
}
