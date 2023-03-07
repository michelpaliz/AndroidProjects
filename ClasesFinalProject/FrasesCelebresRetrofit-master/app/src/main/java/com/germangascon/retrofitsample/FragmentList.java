package com.germangascon.retrofitsample;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.germangascon.retrofitsample.activities.login.ListApdapterGeneric;
import com.germangascon.retrofitsample.interfaces.IListenerList;

import java.util.Arrays;
import java.util.List;

public class FragmentList extends Fragment {

    private IListenerList iListenerList;
    private List<Object> listFrases;
    private List<Object> listCatalogos;
    private List<Object> listAutores;
    private int option;

    public interface IOnAttachListener {
        Object[] getDataFrases();

        Object[] getDataAutores();

        Object[] getDataCategoria();
    }

    public FragmentList() {
        super(R.layout.recycler_view);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            option = bundle.getInt("option", -1);
            System.out.println(option);
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

//        List<Object> objectList = new ArrayList<Object>(nuevaListaAutores);


        switch (option){
            case 1:
                if (listAutores != null) {
                    recyclerView.setAdapter(new ListApdapterGeneric(listAutores, listFrases, iListenerList, option, false));
                }
                break;
            case 2:
                if (listCatalogos != null) {
                    recyclerView.setAdapter(new ListApdapterGeneric(listCatalogos, listFrases, iListenerList, option, false));
                }
                break;

            case 3:
                if (listCatalogos != null) {
                    recyclerView.setAdapter(new ListApdapterGeneric(listFrases, listFrases, iListenerList, option, false));
                }
                break;
        }


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iListenerList = (IListenerList) context;
        IOnAttachListener iOnAttachListener = (IOnAttachListener) context;
        listFrases = Arrays.asList(iOnAttachListener.getDataFrases());
        listCatalogos = Arrays.asList(iOnAttachListener.getDataCategoria());
        listAutores = Arrays.asList(iOnAttachListener.getDataAutores());
    }


}