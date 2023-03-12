package com.example.caminoalba.interfaces;

import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.User;
import com.example.caminoalba.models.dto.UserAndProfileRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface IAPIservice {


    //********* PERSON REST **************

    @POST("person/add")
    Call<Boolean> addPerson(@Body Profile profile);

    @PUT("person/update")
    Call<Boolean> updatePerson(@Body Profile profile);

    @GET("/user/all")
    Call<List<User>> getUsers();

    @POST("user/register")
    Call<UserAndProfileRequest> createUserWithProfile(@Body UserAndProfileRequest userWithProfile);

}
