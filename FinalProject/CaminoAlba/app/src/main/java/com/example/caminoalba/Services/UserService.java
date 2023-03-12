package com.example.caminoalba.Services;

import com.example.caminoalba.rest.RestClient;
import com.example.caminoalba.interfaces.IAPIservice;
import com.example.caminoalba.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  UserService {

    private static final List<User> users = new ArrayList<>();
    private static final IAPIservice iapIservice = RestClient.getInstance();


    public static  void getUsersFromRest(){
        Call<List<User>> usersCall =  iapIservice.getUsers();
        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                assert response.body() != null;
                users.addAll(response.body());
                System.out.println("desde async  " + users);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("Error");
            }
        });
    }

    public static List<com.example.caminoalba.models.User> getUsers() {
        return users;
    }
}
