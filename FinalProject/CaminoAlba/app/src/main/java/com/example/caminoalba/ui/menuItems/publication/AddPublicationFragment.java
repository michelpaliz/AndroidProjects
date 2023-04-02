package com.example.caminoalba.ui.menuItems.publication;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.Gravity;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.example.caminoalba.models.Blog;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.Publication;
import com.example.caminoalba.ui.menuItems.publication.recyclers.RecyclerAdapterPhotos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddPublicationFragment extends Fragment {

    private static final int MAX_PHOTOS = 5; // maximum number of photos allowed

    private EditText etTitle, etDescription;
    private Button btnImage;

    // ------ Para obtener el blog y publicacion  -------
    private Profile profile;
    private Blog blog;
    private String description, title;
    // ------ Para obtener el recyclerview    -------
    private RecyclerAdapterPhotos adapter;
    // ------ Otras referencias    -------
    private List<Uri> uriList;
    private Publication publication;
    private String formattedDate;
    // get a reference to your Firebase database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_publication, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ------ Inicializamos vistas   -------
        // ------ Vistas   -------
        TextView tvName = view.findViewById(R.id.authorName);
        etTitle = view.findViewById(R.id.etTitleField);
        etDescription = view.findViewById(R.id.etDescriptionField);
        btnImage = view.findViewById(R.id.addPhotoButton);
        ImageView imageView = view.findViewById(R.id.authorPhoto);
        TextView tvTimeDisplayed = view.findViewById(R.id.tvTimeDisplayed);
        Button btnAddPublication = view.findViewById(R.id.btnAddPublication);
        // ------ Inicializamos variables  -------
        publication = new Publication();
        uriList = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.rvPhotoGrid);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        // ------ Obtenemos el blog actual  -------
        Gson gson = new Gson();


        // ------  RecyclerView   -------
        adapter = new RecyclerAdapterPhotos(uriList, requireContext());
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 4));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        //SHOW BLOG INFO + PUBLICATION DATA
        String profileStr = preferences.getString("profile", "");
        profile = gson.fromJson(profileStr, Profile.class);

        tvName.setText(profile.getFirstName());
        LocalDateTime datePublished = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        formattedDate = datePublished.format(formatter);
        tvTimeDisplayed.setText(formattedDate);
        Picasso.get().load(profile.getPhoto()).into(imageView);

        Bundle args = getArguments();
        if (args != null) {
            blog = (Blog) args.getSerializable("blog");
        }


        //ADD BUTTONS
        bntAddPhotos();

        if (profile.getPhoto() != null) {
            imageView.setImageURI(Uri.parse(profile.getPhoto()));
        }


        btnAddPublication.setOnClickListener(v -> {

            createPublication();

        });

    }


    //Desde el boton puedo subir varias fotos y guardarlos en un imageview para despues
    //representarlo en el recycler view.

    public void bntAddPhotos() {
        btnImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            uriList.clear(); // Clear the photo list before adding new photos
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                // multiple photos selected
                int count = Math.min(MAX_PHOTOS - uriList.size(), clipData.getItemCount()); // limit number of photos to be added
                for (int i = 0; i < count; i++) {
                    Uri uri = clipData.getItemAt(i).getUri();
                    updateUriList(uri);
                }
            } else {
                // single photo selected
                Uri uri = data.getData();
                updateUriList(uri);
            }
        }
    }

    public void createPublication() {
        // Convert the list of Uri to a list of Strings
        List<String> photoUrls = new ArrayList<>();
        for (Uri uri : uriList) {
            photoUrls.add(uri.toString());
        }

        if (photoUrls.isEmpty()) {
            Toast.makeText(requireContext(), "Please select at least one photo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!title() || !description()) {
            // handle validation errors
            return;
        }

        // Create a new Publication object
        Publication publication = new Publication();

        // Set the attributes of the Publication object
        String publicationId = databaseReference.child("publications").push().getKey();
        publication.setPublication_id(publicationId);
        publication.setTitle(title);
        publication.setDescription(description);
        publication.setDatePublished(formattedDate);
        publication.setBlog(blog);

        // Add the photos to the Publication object
//        publication.setPhotos(photoUrls);

        // Get a reference to the blog in the database
        DatabaseReference blogRef = databaseReference.getDatabase().getReference("blogs/" + profile.getProfile_id());

        // Retrieve the blog from the database
        blogRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if the blog exists in the database
                if (snapshot.exists()) {
                    // Get the blog object from the database
                    Blog existingBlog = snapshot.getValue(Blog.class);

                    // Update the existing blog object with the new publication
                    assert existingBlog != null;
                    existingBlog.addPublication(publication);

                    // Save the updated blog object to Firebase Realtime Database
                    blogRef.setValue(existingBlog);

                    // Update the publication object in the publications array of the blog with the correct ID
                    int index = existingBlog.getPublications().indexOf(publication);
                    if (index != -1) {
                        existingBlog.getPublications().get(index).setPublication_id(publicationId);
                    }

                    uploadPhotos(uriList, publicationId);

                } else {
                    // Handle the case where the blog does not exist in the database
                    Toast.makeText(getContext(), "Blog not found", Toast.LENGTH_SHORT).show();
                    throw new NullPointerException();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database errors
                Log.e(TAG, "Error retrieving blog from Firebase Realtime Database: " + error.getMessage());
            }
        });
    }


    public void updateUriList(Uri uri) {
        if (uriList.size() < MAX_PHOTOS) {
            // Check if the uri already exists in the list
            if (!uriList.contains(uri)) {
                uriList.add(uri);
                adapter.notifyDataSetChanged();
//                uploadPhotos(uriList);
            } else {
                Toast.makeText(requireContext(), "Photo already added", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Maximum number of photos reached", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadPhotos(List<Uri> uris, String publicationId) {
        // Get the Firebase Storage reference with your bucket name
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://caminoalba-3ee10.appspot.com/");
        StorageReference storageRef = storage.getReference();

        // Loop over each photo URI and upload it to Firebase Storage
        for (Uri uri : uris) {
            // Generate a unique name for the image based on the current time
            String imageName = String.valueOf(System.currentTimeMillis());

            // Upload the image to Firebase Storage
            StorageReference imageRef = storageRef.child("publications/" + publicationId + "/" + imageName);

            imageRef.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully, get the download URL
                        imageRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                            // Store the download URL in the photos attribute of the Publication object
                            String downloadUrl = uri1.toString();
                            publication.addPhotos(downloadUrl);
                            Toast.makeText(requireContext(), "Photos updated successfully", Toast.LENGTH_SHORT).show();
                            // Save the updated publication object to Firebase Realtime Database
                            DatabaseReference publicationRef = databaseReference.getDatabase().getReference("publications/" + publicationId);
                            publicationRef.setValue(publication);
                            // Update the UI to show the new publication
                            adapter.notifyDataSetChanged();


                        });
                    })
                    .addOnFailureListener(exception -> {
                        // Handle errors
                        Log.e(TAG, "Error uploading image to Firebase Storage: " + exception.getMessage());
                    });
        }


        //Add publication here
        // In the child fragment:
        // Get the parent FragmentManager instance
        FragmentManager parentFragmentManager = getParentFragmentManager();

        // Define enter and exit animations for the fragment transition
        int enterAnim = R.anim.slide_in_right;
        int exitAnim = R.anim.slide_out_left;

        // Create and set the fragment transition animation object
        Transition fragmentTransition = new Slide(Gravity.START);
        fragmentTransition.setDuration(1000);
        FragmentTransaction fragmentTransaction = parentFragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.setCustomAnimations(enterAnim, exitAnim);
        fragmentTransaction.replace(R.id.fragment_add_publication , new BlogFragment());
        fragmentTransaction.addToBackStack(null);
        Toast.makeText(requireContext(), "Publication Uploaded successfully", Toast.LENGTH_SHORT).show();
        // Commit the fragment transaction
        fragmentTransaction.commit();
    }


    public boolean description() {
        String descriptionStr = etDescription.getText().toString();
        if (descriptionStr.isEmpty()) {
            etDescription.setError("Cannot be empty");
            return false;
        } else {
            etDescription.setError(null);
            description = descriptionStr;
            return true;
        }
    }

    public boolean title() {
        String titleStr = etTitle.getText().toString();
        if (titleStr.isEmpty()) {
            etTitle.setError("Cannot be empty");
            return false;
        } else {
            etTitle.setError(null);
            title = titleStr;
            return true;
        }
    }


}