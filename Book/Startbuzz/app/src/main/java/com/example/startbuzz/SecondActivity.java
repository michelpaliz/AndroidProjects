package com.example.startbuzz;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.startbuzz.Controller.CoffeAdapter;
import com.example.startbuzz.Controller.OpcionesAdapter;
import com.example.startbuzz.Interface.IElement;
import com.example.startbuzz.Model.Coffe;

import java.util.List;

public class SecondActivity extends AppCompatActivity implements IElement {

    private final List<Coffe> coffees;

    public SecondActivity() {
        MainActivity mainActivity = new MainActivity();
        mainActivity.crearDatosDrink();
        this.coffees = mainActivity.getCoffees();

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coffe_list);
        RecyclerView recyclerView = findViewById(R.id.rcListarCoffes);
        recyclerView.setHasFixedSize(true);
        Log.d("lista de cafe", coffees.size()+"");
        recyclerView.setAdapter(new CoffeAdapter(coffees, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


    }


    @Override
    public void onSelectedElement(int position) {
    }

}
