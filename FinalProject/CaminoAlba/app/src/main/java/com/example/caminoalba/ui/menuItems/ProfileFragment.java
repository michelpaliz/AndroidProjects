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

import com.example.caminoalba.R;
import com.example.caminoalba.helpers.Utils;
import com.example.caminoalba.interfaces.IAPIservice;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.User;
import com.example.caminoalba.rest.RestClient;
import com.example.caminoalba.services.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    //  *----- Variables de vistas globales ------*
    private EditText edFirstName, edLastName, edBirthdate, edGender;
    private Date birhtDate;
    private Profile profile;
    private ImageView imgProfile;
    private Button btnSave;
    private Context context;
    private IAPIservice iapiService;

    //  *----- Variables de funcionalidad globales ------*
    private Service service;
    private User user;
    private List<Profile> profileList;
    private List<User> userList;
    private String firstName, lastName, email, password, type, gender;
    private SharedPreferences prefs;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ------ Inicializamos vistas   -------
        edFirstName = view.findViewById(R.id.edFirstName);
        edLastName = view.findViewById(R.id.edLastName);
        edBirthdate = view.findViewById(R.id.edBirthDate);
        edGender = view.findViewById(R.id.edGender);
        btnSave = view.findViewById(R.id.btnSaveInformation);

        // ------ Inicializamos variables  -------
        iapiService = RestClient.getInstance();
        service = new Service();
        profileList = new ArrayList<>();
        userList = new ArrayList<>();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        // ------ Obtenemos el usuario actual mediante este metodo  -------
        getUserList();
        // ------ Obtenemos el perfil actual mediante este metodo   -------
        getProfileList();


    }


//    ********* MOSTRAR DATOS DEL USUARIO ************

    public void showProfileData(Profile profile) {
        edFirstName.setText(profile.getFirstName());
        edLastName.setText(profile.getLastName());

        if (profile.getGender() == null) {
            edGender.setHint("Introduce your gender");
        } else {
            edGender.setText(profile.getGender());
        }

        if (profile.getBirthDate() == null) {
            edBirthdate.setHint("yyyy-MM-dd");
        } else {
            edBirthdate.setText(profile.getBirthDate().toString());
        }
    }


//    ******** MANIPULACION DE DATOS PARA EL USUARIO ***********

    public void getUserList() {
        if (user == null) {
            user = new User();
        }
        service.getUsersFromRest(new Service.APICallback() {
            @Override
            public void onSuccess() {
                // Data is available, do something with it
                userList = service.getUsers();
                // Manipulate the users data here
                getUserByEmail(userList);

            }

            @Override
            public void onFailure(String error) {
                System.out.println(error);
                // Handle error
            }
        });

    }


    public void getUserByEmail(List<User> userList) {
        //Declaramos aqui la inicializacion de la preferencia del email para obtener el email correspondiente
        email = (prefs.getString("emailPref", ""));
        //Encontrar el usuario correspondiente via email
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getEmail().equalsIgnoreCase(email)) {
                user = userList.get(i);
            }
        }
    }


//    ******* MANIPULACION DE DATOS PARA EL PERFIL ******

    public void getProfileList() {
        service.getProfilesFromRest(new Service.APICallback() {
            @Override
            public void onSuccess() {
                profile = new Profile();
                // Data is available, do something with it
                profileList = service.getProfiles();
                // Manipulate the users data here
                getProfileById(profileList);
            }

            @Override
            public void onFailure(String error) {
                System.out.println(error);
                // Handle error
            }
        });

    }


    public void getProfileById(List<Profile> profileList) {
        //Si el perfil ya esta cargado no es necesario generar otra instancia de perfil nuevo
        if (profile == null) {
            profile = new Profile();
        }
        for (int i = 0; i < profileList.size(); i++) {
            System.out.println("Item one by one " + profileList.get(i));
            if (profileList.get(i).getProfile_id() == user.getUser_id()) {
                profile = profileList.get(i);
            }
        }

        // TENEMOS QUE TENER CARGADO EL PERFIL PARA EFECTUAR CAMBIOS

        if (profile == null) {
            throw new NullPointerException();
        }

        showProfileData(profile);
        updateProfile();
    }


    //********** ACTUALIZAR LOS DATOS DEL PERFIL ************

    public void updateProfile() {
        btnSave.setOnClickListener(v -> {
            if (!validateFirstName() || !validateLastName() || !validateDate() || !validateGender()) {
                return;
            }

            profile.setFirstName(firstName);
            profile.setLastName(lastName);
//            java.sql.Date sqlDate = new java.sql.Date(birhtDate.getTime());
            profile.setBirthDate(null);
            profile.setGender(gender);
            profile.setPhoto(null);
            profile.setUser(user);

            Call<Boolean> call = iapiService.updateProfile(profile);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Toast.makeText(getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(getContext(), "Update unsuccessfully", Toast.LENGTH_SHORT).show();
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
            edLastName.setError("Cannot be empty");
            return false;
        } else {
            edLastName.setError(null);
            lastName = edLastName.getText().toString();
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
        System.out.println("esto es del date del usuario " + strNacimiento);
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