package com.example.caminoalba.ui.menuItems;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.caminoalba.R;
import com.example.caminoalba.ui.menuItems.publication.FragmentBlog;

public class FragmentNews extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = new Bundle();
        boolean forAdmin = true;
        FragmentBlog fragmentBlog = new FragmentBlog();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        bundle.putBoolean("isAdmin", forAdmin);
        fragmentBlog.setArguments(bundle);
        transaction.replace(R.id.fragment_news, fragmentBlog);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}