package com.example.caminoalba.ui.menuItems.Partner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.example.caminoalba.models.Path;
import com.example.caminoalba.models.Profile;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;


public class FragmentPartner extends Fragment {
    private List<Path> breakpointsInf;
    private RecyclerView recyclerView;
    private Profile profile;
    private ImageView ivProfilePhoto;
    private TextView tvProfileId, tvProfileName;

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
        ivProfilePhoto = view.findViewById(R.id.profilePhoto);
        tvProfileId = view.findViewById(R.id.tvUserId);
        tvProfileName = view.findViewById(R.id.tvUserName);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        Gson gson = new Gson();
        String profileStr = preferences.getString("profile", "");
        profile = gson.fromJson(profileStr, Profile.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvProfileName.setText(profile.getFirstName().toUpperCase());

        tvProfileId.setText(profile.getProfile_id());

        if (profile.getPhoto() != null){
            Picasso.get().load(profile.getPhoto()).into(ivProfilePhoto);
        }

        // Create a new instance of the RecyclerviewPartner class, passing in the breaksPointsInf HashMap.
        breakpointsInf = getBreakpointsInf();
        if (breakpointsInf != null){
            RecyclerviewPartner adapter = new RecyclerviewPartner(breakpointsInf, profile);
            recyclerView.setAdapter(adapter);
        }


    }


}
