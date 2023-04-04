package com.example.caminoalba.ui.menuItems.profile;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.example.caminoalba.R;
import com.example.caminoalba.helpers.Utils;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;

public class ProfileFragment extends Fragment {

    private final int GALLERY_REQ_CODE = 1000;

    //  *----- Variables de vistas globales ------*
    private EditText edFirstName, edLastName, edBirthdate;
    private Spinner spinnerGender;
    private TextView tvEmailVerfication;
    private ImageView imgProfile;
    private Button btnSave;
    private Context context;
    // ------ Otras referencias    -------
    private Gson gson;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    //  *----- Variables de funcionalidad globales ------*
    private Profile profile;
    private User user;
    private boolean profileUpdated = false;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ------ Inicializamos vistas   -------
        edFirstName = view.findViewById(R.id.edFirstName);
        edLastName = view.findViewById(R.id.edLastName);
        edBirthdate = view.findViewById(R.id.edBirthDate);
        btnSave = view.findViewById(R.id.btnSaveInformation);
        imgProfile = view.findViewById(R.id.imgProfile);
        tvEmailVerfication = view.findViewById(R.id.tvEmailVerified);
        spinnerGender = view.findViewById(R.id.spinnerGender);

        // ------ Inicializamos variables  -------
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();

        // ------ Obtenemos el usuario actual mediante este metodo  -------
        String userStr = prefs.getString("user", "");
        user = gson.fromJson(userStr, User.class);

        // ------ Obtenemos el perfil actual mediante este metodo   -------
        if (!profileUpdated) {
            String profileStr = prefs.getString("profile", "");
            profile = gson.fromJson(profileStr, Profile.class);
            profileUpdated = true;
        }


        //Podemos actualizar el perfil en cualquier momento
        validateGender();
        getUpdatedProfile();
        btnVerifyEmail();
        btnUpdatePhoto();
        btnUpdateProfile();

    }


//    ********* MOSTRAR DATOS DEL USUARIO ************

    public void showProfileData(Profile profile) {

        // Load the photo into the ImageView using Picasso
        editor = prefs.edit();

        if (profile.getPhoto() != null && !profile.getPhoto().isEmpty()) {
            Picasso.get().load(profile.getPhoto()).into(imgProfile);
        } else {
            Picasso.get().load(R.drawable.default_image).into(imgProfile);
        }


        edFirstName.setText(profile.getFirstName());
        edLastName.setText(profile.getLastName());

        if (profile.getBirthDate() == null) {
            edBirthdate.setHint("yyyy-MM-dd");
        } else {
            edBirthdate.setText(profile.getBirthDate());
        }

    }


    public void btnVerifyEmail() {

        if (profile.getUser().getEnabled()) {
            tvEmailVerfication.setText("Email has been verified successfully");
        } else {
            tvEmailVerfication.setText("Email hasn't been verifed, click here to verify it");
            tvEmailVerfication.setOnClickListener(v -> {

                //Create varibles to pass to my child fragment
                Bundle args = new Bundle();
                args.putSerializable("user", user);
                // Create an instance of the child fragment
                ConfirmEmailFragment confirmEmailFragment = new ConfirmEmailFragment();
                //Pass the args already created to the child fragment
                confirmEmailFragment.setArguments(args);
                // Begin a new FragmentTransaction using the getChildFragmentManager() method
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                // Add the child fragment to the transaction and specify a container view ID in the parent layout
                transaction.add(R.id.child_fragment_container, confirmEmailFragment);
                transaction.addToBackStack(null); // Add the fragment to the back stack
                transaction.commit();

            });

        }
    }


    //    ******* MANIPULACION DE DATOS PARA EL PERFIL ******

    public void getUpdatedProfile() {
        // Get the current user from FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("profiles").child(uid);
            profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    profile = dataSnapshot.getValue(Profile.class);
                    assert profile != null;
                    showProfileData(profile);
                    // do something with the retrieved profile
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Couldn't get the profile", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    /**
     * When the ImageView is clicked, an implicit intent Intent.ACTION_PICK is created to select an image from the user's gallery.
     * The android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI specifies the URI for the external storage of images,
     * allowing the user to select an image from their device's gallery.
     */

    public void btnUpdatePhoto() {
        // Moved createPhotoRestPoint method call to a different location

        imgProfile.setOnClickListener(v -> {
            Intent intentGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intentGallery, "Select Picture"), GALLERY_REQ_CODE);
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQ_CODE && data != null) {
            // Get the selected image URI
            Uri uri = data.getData();
            // Display the image in your app
            imgProfile.setImageURI(uri);
//            profile.setPhoto(getImageUri());
            uploadPhoto(uri);
        }
    }


    //********** ACTUALIZAR LOS DATOS DEL PERFIL ************


    public void btnUpdateProfile() {

        btnSave.setOnClickListener(v -> {

            if (!validateFirstName() || !validateLastName() || !validateDate()) {
                return;
            }


            if (profile.getPhoto() != null) {
                if (profile.getPhoto().isEmpty()) {
                    Toast.makeText(getContext(), "Please add at least one photo to update the profile",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            // ================== FIREBASE =================== //
            // Get a reference to the Firebase database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            // Get a reference to the profile you want to update
            DatabaseReference profileRef = database.getReference("profiles/" + profile.getProfile_id());
            // Set the value of the profile object
            profileRef.setValue(profile);
            Toast.makeText(getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
            //Send the data to the other fragment
            Profile profile1 = profile;
            // Convert the JSON string to a Java object using Gson
            // Convert the updated profile object to a JSON string using Gson
            String profileStr = gson.toJson(profile1);
            // Store the updated profile JSON string as a preference
            editor.putString("profile", profileStr);
            editor.apply();

        });
    }


    public void uploadPhoto(Uri uri) {
        // Get the Firebase Storage reference with your bucket name
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://caminoalba-3ee10.appspot.com/");
        StorageReference storageRef = storage.getReference();

        // Upload the image to Firebase Storage
        StorageReference imageRef = storageRef.child("profiles/" + profile.getProfile_id() + "/" + profile.getProfile_id());

        imageRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully, get the download URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                        // Store the download URL in the photo attribute of the Profile object
                        String downloadUrl = uri1.toString();
                        profile.setPhoto(downloadUrl);
                        // Load the image into the ImageView using Picasso or Glide
                        Picasso.get().load(downloadUrl).into(imgProfile);
                        // ================== FIREBASE =================== //
                        // Get a reference to the Firebase database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        // Get a reference to the profile you want to update
                        DatabaseReference profileRef = database.getReference("profiles/" + profile.getProfile_id());
                        // Set the value of the profile object
                        profileRef.setValue(profile);
                        Toast.makeText(getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                        //Send the data to the other fragment
                        Profile profile1 = profile;
                        // Convert the JSON string to a Java object using Gson
                        // Convert the updated profile object to a JSON string using Gson
                        String profileStr = gson.toJson(profile1);
                        // Store the updated profile JSON string as a preference
                        editor.putString("profile", profileStr);
                        editor.apply();
                    });
                })
                .addOnFailureListener(exception -> {
                    // Handle errors
                    Log.e(TAG, "Error uploading image to Firebase Storage: " + exception.getMessage());
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
            String firstName = edFirstName.getText().toString();
            profile.setFirstName(firstName);
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
            String lastName = edLastName.getText().toString();
            profile.setLastName(lastName);
            return true;
        }
    }

    public boolean validateDate() {
        String strNacimiento = edBirthdate.getText().toString();
        System.out.println("esto es del date del usuario " + strNacimiento);
        LocalDate birthday = Utils.validateDate(strNacimiento);
        if (strNacimiento.isEmpty()) {
            edBirthdate.setError("Cannot be empty");
            return false;
        } else if (birthday == null) {
            edBirthdate.setError("Invalid format");
            return false;
        } else {
            edBirthdate.setError(null);
            profile.setBirthDate(strNacimiento);
            return true;
        }
    }


    public void validateGender() {
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = (String) parent.getItemAtPosition(position);
                if (profile != null) {
                    profile.setGender(selectedGender);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

}