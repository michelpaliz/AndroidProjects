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
import android.view.Menu;
import android.view.MenuItem;
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
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.caminoalba.NavigationDrawerActivity;
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
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;


public class FragmentProfile extends Fragment {

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
    private boolean profileUpdated = false;
    private ProfileUpdateListener profileUpdateListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof ProfileUpdateListener) {
            profileUpdateListener = (ProfileUpdateListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ProfileUpdateListener");
        }
    }



    public interface ProfileUpdateListener {
        void onProfileUpdated(Profile profile);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Hide the fragment name from the toolbar
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ------ Init views   -------
        edFirstName = view.findViewById(R.id.edFirstName);
        edLastName = view.findViewById(R.id.edLastName);
        edBirthdate = view.findViewById(R.id.edBirthDate);
        btnSave = view.findViewById(R.id.btnSaveInformation);
        imgProfile = view.findViewById(R.id.imgProfile);
        tvEmailVerfication = view.findViewById(R.id.tvEmailVerified);
        spinnerGender = view.findViewById(R.id.spinnerGender);

        // ------ Init variables  -------
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();

        // ------ We get the user sent by the login activity -------

        String userStr = prefs.getString("user", "");
        User user = gson.fromJson(userStr, User.class);

        // ------ We get the profile sent by the login activity   -------
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


//    ********* SHOW PROFILE DATA ************

    public void showProfileData(Profile profile) {

        // Load the photo into the ImageView using Picasso
        editor = prefs.edit();

        if (profile.getPhoto() != null && !profile.getPhoto().isEmpty()) {
            // Load the image using Picasso
//            Picasso.get().load(profile.getPhoto()).into(imgProfile);

            Glide.with(this)
                    .load(profile.getPhoto())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.default_image) // Placeholder image while loading
                            .error(R.drawable.default_image)) // Image to display in case of error
                    .circleCrop() // Apply circular cropping
                    .into(imgProfile);
        } else {
//            Picasso.get().load(R.drawable.default_image).into(imgProfile);
            Glide.with(this)
                    .load(R.drawable.default_image)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.default_image) // Placeholder image while loading
                            .error(R.drawable.default_image)) // Image to display in case of error
                    .circleCrop() // Apply circular cropping
                    .into(imgProfile);
        }


        edFirstName.setText(profile.getFirstName());
        edLastName.setText(profile.getLastName());

        if (profile.getBirthDate() == null) {
//            edBirthdate.setHint("yyyy-MM-dd");
            edBirthdate.setHint("dd-MM-yyyy");
        } else {
            edBirthdate.setText(profile.getBirthDate());
        }

    }


    public void btnVerifyEmail() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            if (firebaseUser.isEmailVerified()) {
                profile.getUser().setEnabled(true);
                updateUserEnabledInDatabase(profile.getUser());
                tvEmailVerfication.setText(getString(R.string.emailVerified));
            } else {
                tvEmailVerfication.setText(getString(R.string.emailNotVerified));
                tvEmailVerfication.setOnClickListener(v -> {
                    firebaseUser.sendEmailVerification()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), R.string.verification_sent, Toast.LENGTH_SHORT).show();
                                } else {
                                    Exception exception = task.getException();
                                    if (exception instanceof FirebaseFunctionsException) {
                                        FirebaseFunctionsException functionsException = (FirebaseFunctionsException) exception;
                                        FirebaseFunctionsException.Code code = functionsException.getCode();
                                        // Handle specific error codes if needed
                                        String message = functionsException.getMessage();
                                        Toast.makeText(getActivity(), R.string.failed_sending_verification + message, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), R.string.failed_sending_verification, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                });
            }
        }
    }

    private void updateUserEnabledInDatabase(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");
        usersRef.child(user.getUser_id()).child("enabled").setValue(user.getEnabled());
    }


    //    ******* HERE WE MANAGE THE PROFILE DATA ******

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
                    // do something with the retrieved profile
                    showProfileData(profile);
                    String profileStr = gson.toJson(profile);
                    editor.putString("profile", profileStr);
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


    //********** UPDATE PROFILE DATA ************


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
            // After updating the profile, call the listener method
            if (profileUpdateListener != null) {
                profileUpdateListener.onProfileUpdated(profile);
            }
            Toast.makeText(getContext(), R.string.profile_updated_successfully, Toast.LENGTH_SHORT).show();
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
        FirebaseStorage storage = FirebaseStorage.getInstance();
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
                        if (profileUpdateListener != null) {
                            profileUpdateListener.onProfileUpdated(profile);
                        }
                        // Load the image into the ImageView using Picasso or Glide
//                        Picasso.get().load(downloadUrl).into(imgProfile);
                        Glide.with(this)
                                .load(downloadUrl)
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.default_image) // Placeholder image while loading
                                        .error(R.drawable.default_image)) // Image to display in case of error
                                .circleCrop() // Apply circular cropping
                                .into(imgProfile);

                        // ================== FIREBASE =================== //
                        // Get a reference to the Firebase database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        // Get a reference to the profile you want to update
                        DatabaseReference profileRef = database.getReference("profiles/" + profile.getProfile_id());
                        // Set the value of the profile object
                        profileRef.setValue(profile);
                        Toast.makeText(getContext(), R.string.photo_upload_sucessfully, Toast.LENGTH_SHORT).show();
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