package com.example.basedatos;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basedatos.modelos.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


   try

    {
        User user = new User(1, "michael", "1234");
    }catch(
    NoSuchFieldException e)

    {

    }


   //pantalla de login y
}


