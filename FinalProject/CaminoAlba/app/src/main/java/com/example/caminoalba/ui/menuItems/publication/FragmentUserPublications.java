package com.example.caminoalba.ui.menuItems.publication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.caminoalba.R;

public class FragmentUserPublications extends Fragment {

    public FragmentUserPublications() {
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
        return inflater.inflate(R.layout.fragment_user_publications, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = new Bundle();
        FragmentBlog fragmentBlog = new FragmentBlog();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        bundle.putBoolean("userlist", true);
        bundle.putBoolean("comesFromAnotherFragment",true);
        fragmentBlog.setArguments(bundle);
        transaction.replace(R.id.fragment_user_publications, fragmentBlog);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}