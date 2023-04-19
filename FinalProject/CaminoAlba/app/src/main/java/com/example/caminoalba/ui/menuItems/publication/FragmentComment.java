package com.example.caminoalba.ui.menuItems.publication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.Publication;

public class FragmentComment extends Fragment {


    private RecyclerView recyclerView;
    private Publication publication;
    private Profile profile;


    public FragmentComment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = new RecyclerView(requireContext());
        Bundle bundle = getArguments();
        if (getArguments() != null) {
            publication = (Publication) bundle.getSerializable("publication");
            profile = (Profile) bundle.getSerializable("profile");
        }
//        TODO set my adapter comment in order to set up my views
    }



}