package com.example.caminoalba.interfaces;

import androidx.annotation.LongDef;

import com.example.caminoalba.models.Blog;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.User;
import com.example.caminoalba.models.dto.Publication;
import com.example.caminoalba.models.dto.UserAndProfileBlogRequest;

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

    @GET("/blog/all")
    Call<List<Blog>> getBlogs();

    @POST("user/register")
    Call<UserAndProfileBlogRequest> createUserWithProfileBlog(@Body UserAndProfileBlogRequest userWithProfile);

    @PUT("/profile/update")
    Call<Profile> updateProfile(@Body Profile profile);

    @GET("/users/find/{id}")
    Call<User> getUserById(@Path("id") Long id);

    @PUT("/user/update")
    Call<Boolean> updateUser(@Body User user);

    @POST("blog/add/{blogId}/publications")
    Call<Publication> addPublication(@Path("blogId") Long blogId, @Body Publication publication);
    @Multipart
    @POST("/upload-image")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);

    @POST("/publication/add")
    Call<Publication> addPublication(@Body Publication publication);


}
