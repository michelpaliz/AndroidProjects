package com.example.caminoalba.ui.menuItems.Partner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.example.caminoalba.models.Path;

import java.util.HashMap;
import java.util.List;


public class FragmentPartner extends Fragment {
    private List<Path> breakpointsInf;
    private RecyclerView recyclerView;

    public List<Path> getBreakpointsInf() {
        return breakpointsInf;
    }

    public void setBreakpointsInf(List<Path> breakpointsInf) {
        this.breakpointsInf = breakpointsInf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partner, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_partner);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Create a new instance of the RecyclerviewPartner class, passing in the breaksPointsInf HashMap.
        breakpointsInf = getBreakpointsInf();
        if (breakpointsInf != null){
            RecyclerviewPartner adapter = new RecyclerviewPartner(breakpointsInf);
            recyclerView.setAdapter(adapter);
        }

    }


}
