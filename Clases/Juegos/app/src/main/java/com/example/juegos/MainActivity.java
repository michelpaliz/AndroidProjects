package com.example.juegos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{

    private final String TAG = "Message";

    public Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        return super.onOptionsItemSelected(item);
        FragmentManager fragmentManager = getSupportFragmentManager();

        int id = item.getItemId();
        if (id ==  R.id.itemJuego1){
            Toast.makeText(this,"Juego 1", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.fragmento1);
            fragmentManager.beginTransaction().setReorderingAllowed(true).addToBackStack("replacement").replace(R.id.containerFragment1, new FragmentoAhorcado(),null).commit();
//            setContentView(R.layout.activity_main);
            return  true;
        }else if(id == R.id.itemJuego2){
            setContentView(R.layout.fragmento2);
            Toast.makeText(this,"Juego 2", Toast.LENGTH_SHORT).show();
            fragmentManager.beginTransaction().setReorderingAllowed(true).addToBackStack("replacement").replace(R.id.containerFragment2, FragmentoTresRaya.class,null).commit();
        }else{
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}