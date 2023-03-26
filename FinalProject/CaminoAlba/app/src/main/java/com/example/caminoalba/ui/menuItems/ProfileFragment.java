package com.example.caminoalba.ui.menuItems;

import static android.app.Activity.RESULT_OK;

import android.content.ContentValues;
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
import com.example.caminoalba.interfaces.IAPIservice;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.User;
import com.example.caminoalba.rest.RestClient;
import com.example.caminoalba.services.Service;
import com.example.caminoalba.ui.others.ConfirmEmailFragment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private final int GALLERY_REQ_CODE = 1000;

    //  *----- Variables de vistas globales ------*
    private EditText edFirstName, edLastName, edBirthdate;
    private Spinner spinnerGender;
    private TextView tvEmailVerfication;
    private Profile profile;
    private ImageView imgProfile;
    private Button btnSave;
    private Context context;
    private IAPIservice iapiService;

    //  *----- Variables de funcionalidad globales ------*
    private Service service;
    private User user;
    private List<Profile> profileList;
    private String firstName, lastName, gender, photo;
    private Boolean genderSelected;
    private LocalDate birthday;
    private SharedPreferences.Editor editor;
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
        btnSave = view.findViewById(R.id.btnSaveInformation);
        imgProfile = view.findViewById(R.id.imgProfile);
        tvEmailVerfication = view.findViewById(R.id.tvEmailVerified);
        spinnerGender = view.findViewById(R.id.spinnerGender);

        // ------ Inicializamos variables  -------
        genderSelected = false;
        iapiService = RestClient.getInstance();
        service = new Service();
        profileList = new ArrayList<>();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();

        // ------ Obtenemos el usuario actual mediante este metodo  -------
        String userStr = prefs.getString("user", "");
        user = gson.fromJson(userStr, User.class);

        // ------ Obtenemos el perfil actual mediante este metodo   -------
        String profileStr = prefs.getString("profile", "");
        profile = gson.fromJson(profileStr, Profile.class);

        photo = prefs.getString("photo", "");

        //Cuando anyadimos una nueva foto cargamos el perfil actualizado
        if (profile.getPhoto() != null) {
            if (!profile.getPhoto().equalsIgnoreCase(photo)) {
                getUpdatedProfile();
            }
        }

        //Si cumple con las condiciones podemos actualizar el genero con el perfil
        if (!Objects.equals(profile.getGender(), gender) || profile.getGender() == null) {
            btnValidateGender();
        }


        showProfileData(profile);
        showEmailVerificationStatus();
        //Podemos actualizar el perfil en cualquier momento
        btnUpdatePhoto();
        btnUpdateProfile();


    }

//    ********* MOSTRAR DATOS DEL USUARIO ************

    public void showProfileData(Profile profile) {

        // Load the photo into the ImageView using Picasso
        editor = prefs.edit();
        photo = prefs.getString("photo", "");
        if (profile.getPhoto() != null && !profile.getPhoto().isEmpty()) {
            Picasso.get().load(profile.getPhoto()).into(imgProfile);
        } else {
            Picasso.get().load(R.drawable.default_image).into(imgProfile);
            // Load the photo into the ImageView using Glide
            //Glide.with(context).load(photo).into(imgProfile);
        }

        edFirstName.setText(profile.getFirstName());
        edLastName.setText(profile.getLastName());

        if (profile.getBirthDate() == null) {
            edBirthdate.setHint("yyyy-MM-dd");
        } else {
            edBirthdate.setText(profile.getBirthDate());
        }

    }


    public void showEmailVerificationStatus() {

        if (user.getEnabled()) {
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
        showProfileData(profile);
//        btnUpdateProfile();
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
            if (photo != null){
                createPhotoRestPoint();
            }
        });
    }

    /**
     * The createPhotoRestPoint() method is used to create a REST endpoint for uploading the user's profile photo.
     * The method first converts the ImageView's drawable to a bitmap and then saves it as a file in the app's cache directory.
     * It then creates a RequestBody object with the image file and creates a MultipartBody.Part object to upload the file to the server using a Retrofit service.
     */
    public void createPhotoRestPoint() {
        // Get the image from the ImageView
        Drawable drawable = imgProfile.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        // Create a temporary file to store the image
        File file = null;
        try {
            file = File.createTempFile("image", ".jpg", requireContext().getCacheDir());
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the request body and file part
        assert file != null;
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        // Call the API to upload the file
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
            if (photo != null) {
                editor.putString("photo", profile.getPhoto());
            }
        }
    }

    /**
     * The getImageUri() method is used to convert a Bitmap image to a Uri that can be used to store or share the image.
     * The method first checks the type of the Drawable in the imgProfile ImageView, whether it is a BitmapDrawable or a VectorDrawable.
     * If it is a BitmapDrawable, the Bitmap is retrieved from the Drawable and passed to the getImageUri(Bitmap) method.
     * If it is a VectorDrawable, a new Bitmap is created with the same dimensions as the VectorDrawable, and the VectorDrawable is drawn onto the Canvas of the Bitmap.
     * The resulting Bitmap is then passed to the getImageUri(Bitmap) method.
     */
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
        return imageUri != null ? imageUri.toString() : null;
    }
    /**
     * @param bitmap
     * @return This method creates a new ContentValues object and sets the display name and MIME type for the image.
     * It then uses the getContentResolver method to insert the image into the MediaStore database and get its Uri.
     * Finally, it opens an output stream to the Uri and writes the Bitmap to it.
     * We ensure that the file is being saved has a unique name using timestamp or a unique identifier to the file name to make it unique.
     */

    public Uri getImageUri(Bitmap bitmap) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "profile_" + timestamp + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        Uri uri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        OutputStream outputStream;
        try {
            outputStream = requireContext().getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Objects.requireNonNull(outputStream).close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return uri;
    }

    //********** ACTUALIZAR LOS DATOS DEL PERFIL ************

    public void btnUpdateProfile() {
        btnSave.setOnClickListener(v -> {
            if (!validateFirstName() || !validateLastName() || !validateDate() || !btnValidateGender()) {
                return;
            }

            profile.setFirstName(firstName);
            profile.setLastName(lastName);
            profile.setBirthDate(birthday.toString());
            if (!photo.isEmpty()) {
                profile.setPhoto(photo);
            }
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

    public boolean btnValidateGender() {
        spinnerGender.setPrompt("Select gender");
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Do something with the selected gender, such as save it to a variable or display it in a TextView
                gender = (String) adapterView.getItemAtPosition(position);
                if (gender != null && !gender.isEmpty() && !gender.equalsIgnoreCase(profile.getGender())) {
                    profile.setGender(gender);
                }
                genderSelected = true;
                btnUpdateProfile();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                genderSelected = false;
            }
        });


        return genderSelected;
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

