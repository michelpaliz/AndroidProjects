package com.example.caminoalba.ui.menuItems;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.caminoalba.R;
import com.example.caminoalba.models.Person;
import com.example.caminoalba.models.User;

public class ProfileFragment extends Fragment {


    private EditText edFirstName, edLastName, edBirthdate, edGender;
    private ImageView imgProfile;
    private User user;
    private Context context;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edFirstName = view.findViewById(R.id.edFirstName);
        edLastName = view.findViewById(R.id.edLastName);
        edBirthdate = view.findViewById(R.id.edBirthDate);
        edGender = view.findViewById(R.id.edGender);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String firstName, lastName, email, password, type;

        firstName = (prefs.getString("usernamePref", ""));
        lastName = (prefs.getString("lastNamePref", ""));
        password = (prefs.getString("passwordPref", ""));
        email = (prefs.getString("emailPref", ""));
        type = (prefs.getString("typePref", ""));
        user = new User(firstName, lastName, email, password, type, new Person(firstName, lastName, null, null, null));


        if (user.getPerson().getBirthDate() == null){
            edBirthdate.setHint("Introduce your birhtdate");
        }
        if (user.getPerson().getGender() == null){
            edGender.setHint("Introduce your gender");
        }

        edFirstName.setText(user.getFirstName().toUpperCase());
        edLastName.setText(user.getLastName().toUpperCase());
    }


}