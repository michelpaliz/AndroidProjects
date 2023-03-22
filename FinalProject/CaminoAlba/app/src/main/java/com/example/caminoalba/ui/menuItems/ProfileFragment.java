package com.example.caminoalba.ui.menuItems;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.example.caminoalba.R;
import com.example.caminoalba.helpers.Utils;
import com.example.caminoalba.interfaces.IAPIservice;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.User;
import com.example.caminoalba.rest.RestClient;
import com.example.caminoalba.services.Service;
import com.example.caminoalba.ui.others.ConfirmEmailFragment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private final int GALLERY_REQ_CODE = 1000;

    //  *----- Variables de vistas globales ------*
    private EditText edFirstName, edLastName, edBirthdate, edGender;
    private TextView tvEmailVerfication;
    private Profile profile;
    private ImageView imgProfile;
    private Button btnSave;
    private Context context;
    private IAPIservice iapiService;
    private Gson gson;

    //  *----- Variables de funcionalidad globales ------*
    private Service service;
    private User user;
    private List<Profile> profileList;
    private List<User> userList;
    private String firstName, lastName, email, gender, photo;
    private LocalDate birthday;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


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
        imgProfile = view.findViewById(R.id.imgProfile);
        tvEmailVerfication = view.findViewById(R.id.tvEmailVerified);

        // ------ Inicializamos variables  -------
        iapiService = RestClient.getInstance();
        service = new Service();
        profileList = new ArrayList<>();
        userList = new ArrayList<>();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();

        // ------ Obtenemos el usuario actual mediante este metodo  -------
        getUserList();
        // ------ Obtenemos el perfil actual mediante este metodo   -------
        getProfileList();

        // ------ Obtenemos la foto por medio del perfil ya obtenido    -------
        uploadPhoto();

        SharedPreferences profilePref = android.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = profilePref.edit();
        photo = profilePref.getString("photo", "");
        System.out.println("esto es photo " + photo);
        // Load the photo into the ImageView using Picasso
        if (!photo.isEmpty()) {
            Picasso.get().load(photo).into(imgProfile);
        }
        // Load the photo into the ImageView using Glide
//        Glide.with(context).load(photo).into(imgProfile);


    }


    public void showEmailVerificationStatus() {

        if (user.getEnabled()) {
            tvEmailVerfication.setText("Email has been verified successfuly");
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

        String userStr = gson.toJson(user);
        editor.putString("user", userStr);
        showEmailVerificationStatus();
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

    //    ******* MANIPULACION DE DATOS PARA EL FOTO DEL PERFIL ******

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

        String profileStr = gson.toJson(profile);
        editor.putString("profile", profileStr);

        editor.putString("photo", profile.getPhoto());
        editor.putString("gender", profile.getGender());
        editor.putString("birthDate", profile.getBirthDate());
        editor.commit();
        editor.apply();
        showProfileData(profile);

        updateProfile();
    }


    public void uploadPhoto() {
        if (photo == null) {
            imgProfile.setVisibility(View.GONE);
            imgProfile.setOnClickListener(v -> {
                Intent intentGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intentGallery, "Select Picture"), GALLERY_REQ_CODE);
                imgProfile.setVisibility(View.VISIBLE);
                System.out.println("Check if it is null" + imgProfile.getDrawable());
                if ((imgProfile.getDrawable() != null)) {
                    createPhotoRestPoint();
                }
            });
        }

        imgProfile.setOnClickListener(v1 -> {
            Intent intentGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intentGallery, "Select Picture"), GALLERY_REQ_CODE);

        });


    }


    public void createPhotoRestPoint() {
//        To upload an image from an ImageView in your app, you'll need to convert the image to a File object first
        Drawable drawable = imgProfile.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//        int cont = 0;
//        cont++;
        File file = new File(requireContext().getCacheDir(), "image.png");
        try {
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        service.savePhotoLocalServer(new Service.APICallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Photo sent successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getContext(), "Photo sent unsuccessfully", Toast.LENGTH_SHORT).show();
            }
        }, filePart);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQ_CODE && data != null) {
            // Get the selected image URI
            Uri uri = data.getData();

            // Display the image in your app
            imgProfile.setImageURI(uri);
            photo = getImageUri();
        }
    }


    public String getImageUri() {
        Uri imageUri = null;
        Drawable drawable = imgProfile.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            imageUri = getImageUri(bitmap);
        } else if (drawable instanceof VectorDrawable) {
            VectorDrawable vectorDrawable = (VectorDrawable) drawable;
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
            imageUri = getImageUri(bitmap);
        }
        assert imageUri != null;
        return imageUri.toString();
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(requireContext().getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }


    //********** ACTUALIZAR LOS DATOS DEL PERFIL ************

    public void updateProfile() {
        btnSave.setOnClickListener(v -> {
            if (!validateFirstName() || !validateLastName() || !validateDate() || !validateGender()) {
                return;
            }

            profile.setFirstName(firstName);
            profile.setLastName(lastName);
            System.out.println("This is birthday " + birthday);
//            profile.setBirthDate(edBirthdate.getText().toString());
            profile.setBirthDate(birthday.toString());
            profile.setGender(gender);
            profile.setPhoto(photo);
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
        birthday = Utils.validateDate(strNacimiento);
//        assert date != null;
//        birthday = Utils.convertDateToSQLDATE(date);
        if (strNacimiento.isEmpty()) {
            edBirthdate.setError("Cannot be empty");
            return false;
        } else if (birthday == null) {
            edBirthdate.setError("Invalid format");
            return false;
        } else {
            edBirthdate.setError(null);
            return true;
        }
    }


}