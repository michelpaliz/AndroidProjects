package com.germangascon.retrofitsample.interfaces;

import com.germangascon.retrofitsample.models.Autor;
import com.germangascon.retrofitsample.models.Categoria;
import com.germangascon.retrofitsample.models.Frase;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface IAPIService {


    //********* FRASES REST **************

    @GET("frase/all")
    Call<List<Frase>> getFrases();

    @POST("frase/add")
    Call<Boolean> addFrase(@Body Frase frase);

    @POST("frase/addValues")
    @FormUrlEncoded
    Call<Boolean> addFraseValues(@Field("texto") String texto,
                                 @Field("fechaProgramada") String fechaProgramada,
                                 @Field("idAutor") int idAutor,
                                 @Field("idCategoria") int idCategoria);


    //********* AUTORES REST **************
    @GET("autor/all")
    Call<List<Autor>> getAutores();

    @POST("autor/add")
    Call<Boolean> addAutor(@Body Autor autor);

    //********* CATEGORIA REST **************

    @GET("categoria/all")
    Call<List<Categoria>> getCategorias();

    @POST("categoria/add")
    Call<Boolean> addCategoria(@Body Categoria categoria);



    @PUT("autor/update")
    Call<Boolean> updateAutor(@Body Autor autor);


    @PUT("categoria/update")
    Call<Boolean> updateCategoria(@Body Categoria categoria);

    @PUT("frase/update")
    Call<Boolean> updateFrase(@Body Frase frase);

}
