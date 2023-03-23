package com.example.caminoalba.services;

import androidx.annotation.NonNull;

import com.example.caminoalba.interfaces.IAPIservice;
import com.example.caminoalba.models.Blog;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.User;
import com.example.caminoalba.rest.RestClient;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Service {

    public List<User> users;
    public List<Profile> profiles;
    private List<Blog> blogs;
    private APICallback apiCallback;
    private final IAPIservice iapIservice = RestClient.getInstance();

    public interface APICallback {
        void onSuccess();

        void onFailure(String error);
    }

    public void getUsersFromRest(APICallback callback) {
        Call<List<User>> usersCall = iapIservice.getUsers();
        usersCall.enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(@NonNull Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    users = response.body();
                    callback.onSuccess();
                } else {
                    callback.onFailure("Failed to get users");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void getProfilesFromRest(APICallback callback) {
        Call<List<Profile>> profilesCall = iapIservice.getProfiles();
        profilesCall.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                if (response.isSuccessful()) {
                    profiles = response.body();
                    callback.onSuccess();
                } else {
                    callback.onFailure("Failed to get profiles");
                }
            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void getBlogsFromRest(APICallback callback) {
        Call<List<Blog>> blogCall = iapIservice.getBlogs();
        blogCall.enqueue(new Callback<List<Blog>>() {
            @Override
            public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                if (response.isSuccessful()) {
                    blogs = response.body();
                    callback.onSuccess();
                } else {
                    callback.onFailure("Failed to get blogs");
                }
            }

            @Override
            public void onFailure(Call<List<Blog>> call, Throwable t) {
                callback.onFailure("Failed to get blogs");
            }
        });
    }


    public void savePhotoLocalServer(APICallback apiCallback, MultipartBody.Part multipartBody) {

        Call<ResponseBody> call = iapIservice.uploadImage(multipartBody);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Handle success response
                apiCallback.onSuccess();
                System.out.println("Photo sent successfully");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle error
                System.out.println("Photo sent unsuccessfully ");
                apiCallback.onFailure(t.getMessage());
            }
        });
    }


    public List<User> getUsers() {
        return users;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }
}

