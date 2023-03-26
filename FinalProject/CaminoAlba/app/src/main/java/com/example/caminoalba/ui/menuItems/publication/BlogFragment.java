package com.example.caminoalba.ui.menuItems.publication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.example.caminoalba.R;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.User;
import com.google.gson.Gson;

public class BlogFragment extends Fragment {

    private EditText etSearchBar;;
    private FrameLayout frameLayout;
    private SharedPreferences preferences;
    private User user;
    private Profile profile;
    private ImageView imgHome, imgBlog, imgAddPublication;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ------ Inicializamos vistas   -------
        etSearchBar = view.findViewById(R.id.etSearch_bar);
        frameLayout = view.findViewById(R.id.recycler_view_container);
        imgHome = view.findViewById(R.id.imgHome);
        imgBlog = view.findViewById(R.id.imgBlog);
        imgAddPublication = view.findViewById(R.id.imgAddPublication);
        // ------ Inicializamos variables  -------
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        user = new User();
        profile = new Profile();
        Gson gson = new Gson();
        String userStr = preferences.getString("user", "");
        String profileStr = preferences.getString("profile", "");
        user = gson.fromJson(userStr, User.class);
        profile = gson.fromJson(profileStr, Profile.class);

        handleFragments();
    }

    public void handleFragments() {



        imgBlog.setOnClickListener(v -> {

        });

        imgHome.setOnClickListener(v -> {

        });

        imgAddPublication.setOnClickListener(v -> {
            //Create varibles to pass to my child fragment
            Bundle args = new Bundle();
            args.putSerializable("user", user);
            // Create an instance of the child fragment
            AddPublicationFragment addPublicationFragment = new AddPublicationFragment();
            //Pass the args already created to the child fragment
            addPublicationFragment.setArguments(args);
            // Begin a new FragmentTransaction using the getChildFragmentManager() method
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            // Add the child fragment to the transaction and specify a container view ID in the parent layout
            transaction.add(R.id.fragment_blog, addPublicationFragment);
            transaction.addToBackStack(null); // Add the fragment to the back stack
            transaction.commit();
        });

    }


}