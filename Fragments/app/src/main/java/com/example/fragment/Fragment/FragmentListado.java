package com.example.fragment.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fragment.Adapter.AdaptadorCarta;
import com.example.fragment.Interface.IListener;
import com.example.fragment.Model.Letter;
import com.example.fragment.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentListado extends Fragment {
    private List<Letter> cartas;
    private IListener listener;

    public FragmentListado() {
        super(R.layout.fragment_listado);
    }

    @Nullable
    public View onCreatedView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedIntanceState){
        return super.onCreateView(inflater,container,savedIntanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        //Buscamos nuestro listView
        ListView lsListado = view.findViewById(R.id.lsListado);
        //
        inicializador();
        //Enlazamos nuestro listView con nuestro Fragment y nuestro Adapter a la vez
        lsListado.setAdapter(new AdaptadorCarta(this, cartas));
        //Para que funcione necesitamos crear un listener para poder cojer nuestro dato
        lsListado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (listener!=null){
                    listener.letterSelected((Letter) adapterView.getItemAtPosition(position));
                }
            }
        });

    }

    public void inicializador(){
        Letter carta;
        cartas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            carta = new Letter("Propietario "+i,"Detalle" + i,"Dato " + i );
            cartas.add(carta);
        }
    }

    public List<Letter> getCartas() {
        return cartas;
    }

    public void setListener(IListener listener) {
        this.listener = listener;
    }


}
