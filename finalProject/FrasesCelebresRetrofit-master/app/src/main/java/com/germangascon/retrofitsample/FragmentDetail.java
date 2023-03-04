package com.germangascon.retrofitsample;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.germangascon.retrofitsample.activities.login.ListApdapterGeneric;
import com.germangascon.retrofitsample.models.Autor;
import com.germangascon.retrofitsample.models.Categoria;
import com.germangascon.retrofitsample.models.Frase;

import java.util.ArrayList;
import java.util.List;

public class FragmentDetail extends Fragment {

    private Object tipo;
    private List<Frase> listFrases;
    private List<Frase> filterFrases;
    private int option;

    public interface IOnAttachListener{
        Object getDataForDetail();
        List<Frase> getFrases();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.recycler_view, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            option = bundle.getInt("option", -1);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (option == 1){
            filterFrases = new ArrayList<>();
            Autor autor = (Autor) tipo;
            for (Frase frase : listFrases) {
                if (frase.getAutor().getId() == autor.getId() ) {
                    filterFrases.add(frase);
                }
            }
        }else if (option == 2){
            filterFrases = new ArrayList<>();
            Categoria categoria = (Categoria) tipo;
            for (Frase frase : listFrases) {
                if (frase.getCategoria().getId() == categoria.getId() ) {
                    filterFrases.add(frase);
                }
            }
        }

        System.out.println("autor tiene " + filterFrases.size());
        System.out.println("autor frases " + filterFrases);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new ListApdapterGeneric(null, filterFrases, null, 0, true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener attachListener = (IOnAttachListener) context;
        tipo = attachListener.getDataForDetail();
        listFrases = attachListener.getFrases();
    }
}