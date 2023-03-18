package com.example.caminoalba.interfaces;

import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.User;
import com.example.caminoalba.models.dto.UserAndProfileRequest;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface IAPIservice {


    //********* PERSON REST **************

    @GET("/profile/all")
    Call<List<Profile>> getProfiles();

    @GET("/user/all")
    Call<List<User>> getUsers();

    @POST("user/register")
    Call<UserAndProfileRequest> createUserWithProfile(@Body UserAndProfileRequest userWithProfile);

    @PUT("/profile/update")
    Call<Boolean> updateProfile(@Body Profile profile);

    @Multipart
    @POST("/upload-image")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);
}