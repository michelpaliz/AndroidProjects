package com.example.caminoalba.services;
import androidx.annotation.NonNull;

import com.example.caminoalba.interfaces.IAPIservice;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.User;
import com.example.caminoalba.rest.RestClient;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Service {

    public List<User> users;
    public List<Profile> profiles;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }
}

