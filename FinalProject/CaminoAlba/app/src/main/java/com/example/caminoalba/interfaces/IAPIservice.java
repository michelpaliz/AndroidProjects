package com.example.caminoalba.interfaces;

import com.example.caminoalba.models.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface IAPIservice {


    //********* PERSON REST **************

    @POST("person/add")
    Call<Boolean> addPerson(@Body Person person);
    @PUT("person/update")
    Call<Boolean> updatePerson(@Body Person person);



}
