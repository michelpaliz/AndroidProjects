package com.germangascon.retrofitsample.interfaces;

import com.germangascon.retrofitsample.models.Frase;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IAPIService {
    @GET("frase/all")
    Call<List<Frase>> getFrases();

    @POST("frase/add")
    Call<Boolean> addFrase(@Body Frase frase);

    @POST("frase/addValues")
    @FormUrlEncoded
    Call<Boolean> addFraseValues(@Field("texto") String texto,
                                 @Field("fechaProgramada") String fechaProgramada,
                                 @Field("idAutor") int idAutor,
                                 @Field("idCategoria")int idCategoria);

}
