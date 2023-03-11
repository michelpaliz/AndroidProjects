package com.example.caminoalba.ui.menuItems;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.caminoalba.Config.RestClient;
import com.example.caminoalba.Config.Utils;
import com.example.caminoalba.R;
import com.example.caminoalba.Services.UserService;
import com.example.caminoalba.interfaces.IAPIservice;
import com.example.caminoalba.models.Person;
import com.example.caminoalba.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {


    private EditText edFirstName, edLastName, edBirthdate, edGender;
    private String firstName, lastName, gender;
    private Date birhtDate;
    private Person person;
    private ImageView imgProfile;
    private Button btnSave;
    private User user;
    private Context context;
    private IAPIservice iapiService;
    private List<User> userList = new ArrayList<>();


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
        btnSave = view.findViewById(R.id.btnSaveInformation);
        iapiService = RestClient.getInstance();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String firstName, lastName, email, password, type;

        firstName = (prefs.getString("usernamePref", ""));
        lastName = (prefs.getString("lastNamePref", ""));
        password = (prefs.getString("passwordPref", ""));
        email = (prefs.getString("emailPref", ""));
        type = (prefs.getString("typePref", ""));

        person = new Person(firstName, lastName, null, null, null);

        user = new User(firstName, lastName, email, password, type, person);

        UserService.getUsersFromRest();


        System.out.println("Esto es la lista desde el fragmento" + UserService.getUsers());

        List<User> userList = UserService.getUsers();

        for (int i = 0; i < userList.size() ; i++) {
            System.out.println("item " + userList.get(i).getPerson());
        }

//        System.out.println("Esto un item de la lista desde el fragmento" + userList);
//        System.out.println("Esto un item de la lista desde el fragmento" + UserService.getUsers().get(1).getEmail());


        if (user.getPerson().getBirthDate() == null) {
            edBirthdate.setHint("dd-MM-yyyy");
        } else {
            edBirthdate.setText(user.getPerson().getBirthDate().toString());
        }
        if (user.getPerson().getGender() == null) {
            edGender.setHint("Introduce your gender");
        } else {
            edGender.setText(user.getPerson().getGender());
        }

        edFirstName.setText(user.getFirstName().toUpperCase());
        edLastName.setText(user.getLastName().toUpperCase());

        updatePerson();
    }





    public void updatePerson() {
        btnSave.setOnClickListener(v -> {
            if (!validateFirstName() || !validateLastName() || !validateDate() || !validateGender()) {
                return;
            }

            if (user.getPerson() != null) {
//                TODO put reques to update the user's person
            }

            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setBirthDate(birhtDate);
            person.setGender(gender);
            person.setPhoto(null);

            System.out.println(user);

            iapiService.addPerson(user.getPerson()).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Toast.makeText(getContext(), "Profile modified successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(getContext(), "Sorry there was an error try again later", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

//    **************** VALIDATE DATA *****************

    public boolean validateFirstName() {
        String strFirstName = edFirstName.getText().toString();
        if (strFirstName.isEmpty()) {
            edFirstName.setError("Cannot be empty");
            return false;
        } else {
            edFirstName.setError(null);
            firstName = edFirstName.getText().toString();
            return true;
        }
    }

    public boolean validateLastName() {
        String strLastName = edLastName.getText().toString();
        if (strLastName.isEmpty()) {
            edFirstName.setError("Cannot be empty");
            return false;
        } else {
            edFirstName.setError(null);
            lastName = edFirstName.getText().toString();
            return true;
        }
    }

    public boolean validateGender() {
        String genderStr = edGender.getText().toString();
        if (genderStr.isEmpty()) {
            edGender.setError("Cannot be empty");
            return false;
        } else {
            edGender.setError(null);
            gender = edGender.getText().toString();
            return true;
        }
    }

    public boolean validateDate() {
        String strNacimiento = edBirthdate.getText().toString();
        birhtDate = Utils.validateDate(strNacimiento);

        if (strNacimiento.isEmpty()) {
            edBirthdate.setError("Cannot be empty");
            return false;
        } else if (birhtDate == null) {
            edBirthdate.setError("Invalid format");
            return false;
        } else {
            edBirthdate.setError(null);
            return true;
        }
    }


}