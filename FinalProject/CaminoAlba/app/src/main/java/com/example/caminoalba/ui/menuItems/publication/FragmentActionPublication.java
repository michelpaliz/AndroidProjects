package com.example.caminoalba.ui.menuItems.publication;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.example.caminoalba.models.Blog;
import com.example.caminoalba.models.Publication;
import com.example.caminoalba.ui.menuItems.FragmentNews;
import com.example.caminoalba.ui.menuItems.publication.recyclers.RecyclerAdapterAddPhotos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FragmentActionPublication extends Fragment {

    private static final int MAX_PHOTOS = 5; // maximum number of photos allowed

    private EditText etTitle, etDescription;
    private Button btnImage;
    private String placemarkName;
    private boolean isAdmin, isNews, isBlog;
    private Publication publication;
    private boolean isEdit;
    private Button btnAddPublication;

    // ------ Para obtener el blog y publicacion  -------
    private Blog blog;
    private String description, title;
    // ------ Para obtener el recyclerview    -------
    private RecyclerAdapterAddPhotos adapter;
    // ------ Otras referencias    -------
    private List<Uri> uriList;
    private String formattedDate;
    // get a reference to your Firebase database
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

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
        // ------ Init Views  -------
        TextView tvName = view.findViewById(R.id.authorName);
        etTitle = view.findViewById(R.id.etTitleField);
        etDescription = view.findViewById(R.id.etDescriptionField);
        btnImage = view.findViewById(R.id.addPhotoButton);
        ImageView imageView = view.findViewById(R.id.authorPhoto);
        TextView tvTimeDisplayed = view.findViewById(R.id.tvTimeDisplayed);
        btnAddPublication = view.findViewById(R.id.btnAddPublication);
        // ------ Init variables -------
        uriList = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.rvPhotoGrid);

        // ------  RecyclerView   -------
        if (uriList != null) {
            adapter = new RecyclerAdapterAddPhotos(uriList, requireContext());
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 4));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        // *** GETTING ARGUMENTS **** //
        assert getArguments() != null;

        //This arguments comes from the blog Fragment, also the blog is always updated with the current profile and user ?
        blog = (Blog) getArguments().getSerializable("blog");
        isBlog = getArguments().getBoolean("isBlog", false);
        placemarkName = getArguments().getString("placemark");

        //This arguments comes from the RecyclerAdapterPublications
        isEdit = getArguments().getBoolean("edit", false);
        isNews = getArguments().getBoolean("isNews", false);
        String publicationJson = getArguments().getString("publication");
        if (publicationJson != null) {
            Gson gson = new Gson();
            publication = gson.fromJson(publicationJson, Publication.class);
        }

        //If we are editing the publication we got the publication's blog object and also we check if the user is admin
        if (blog == null) {
            blog = publication.getBlog();
        }

        if (blog.getProfile().getUser().getType().equalsIgnoreCase("admin")) {
            isAdmin = true;
        }

        //SHOW BLOG INFO + PUBLICATION DATA
        tvName.setText(blog.getProfile().getFirstName());
        LocalDateTime datePublished = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        formattedDate = datePublished.format(formatter);
        tvTimeDisplayed.setText(formattedDate);

        //Get the current profile photo and show it
        if (blog.getProfile().getPhoto() != null) {
            imageView.setImageURI(Uri.parse(blog.getProfile().getPhoto()));
        } else {
            imageView.setImageResource(R.drawable.default_image);
        }

        //If it's news then we must check if the user is admin if not then it must be blog fragment
        if (isEdit && publication != null) {
            //First thing we need to retrieve the old data show the user what he is goint to change
            etTitle.setText(publication.getTitle());
            etDescription.setText(publication.getDescription());

            btnAddPublication.setText("Edit Publication");
            if (publication.getPhotos() != null || !publication.getPhotos().isEmpty()) {
                for (String url : publication.getPhotos()) {
                    Uri uri = Uri.parse(url);
                    uriList.add(uri);
                }
                adapter = new RecyclerAdapterAddPhotos(uriList, requireContext());
                recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 4));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }

        btnPublicationActions();
        bntAddPhotos();
    }


    public void btnPublicationActions() {
        btnAddPublication.setOnClickListener(v -> {
            if (isNews) {
                if (isEdit && publication != null && isAdmin) {
                    editPublication(publication);
                }
            } else {
                if (isEdit && publication != null) {
                    editPublication(publication);
                }
                if (!isEdit) {
                    createPublication();
                }
            }
        });
    }


    // ****** PHOTO SECTION *******

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
//            uriList.clear();
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

    //    In this modified implementation, if the user is editing an existing publication (edit is true), and the publication already has photos (publication.getPhotos() != null && !publication.getPhotos().isEmpty()), then the old photos are cleared before adding the new ones.
    //    Otherwise, the new photo is simply added to the list.
    public void updateUriList(Uri uri) {
        if (uriList.size() < MAX_PHOTOS) {
            // Check if the uri already exists in the list
            if (!uriList.contains(uri)) {
                uriList.add(uri);
                //We need to notify the adapter when we are adding a new photo when we press the button add Photo
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(requireContext(), "Photo already added", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Maximum number of photos reached", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * @param publication The publication that we wan to edit
     */

    public void editPublication(Publication publication) {

        // Retrieve the list of photo URLs currently associated with the publication

        if (publication.getPhotos() == null) {
            publication.setPhotos(new ArrayList<>());
        }

        List<String> photoUrls = publication.getPhotos();

        // Convert the list of Uri to a list of Strings
        List<String> newPhotoUrls = new ArrayList<>();
        for (Uri uri : uriList) {
            newPhotoUrls.add(uri.toString());
        }

        // Check for removed photos and delete them from Firebase Storage
        for (String oldPhotoUrl : photoUrls) {
            if (!newPhotoUrls.contains(oldPhotoUrl)) {
                // Get a reference to the image file in Firebase Storage
                StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(oldPhotoUrl);

                // Delete the image file from Firebase Storage
                imageRef.delete().addOnSuccessListener(aVoid -> {
                    // Image deleted successfully, remove the URL from the publication object
                    //This code below will first search for the index of the oldPhotoUrl in the photoUrls list using the indexOf method. If the oldPhotoUrl exists in the list, the indexOf method will return its index, which will be stored in the index variable.
                    //If the oldPhotoUrl is not found in the list, indexOf will return -1.
                    int index = publication.getPhotos().indexOf(oldPhotoUrl);
                    if (index != -1) {
                        photoUrls.remove(index);
                    }

                    // Update the publication object in the database with the new list of photo URLs
                    DatabaseReference publicationRef = databaseReference.getDatabase().getReference("publications/" + publication.getPublication_id());
                    publicationRef.setValue(publication);
                }).addOnFailureListener(exception -> {
                    // Handle errors
                    Log.e(TAG, "Error deleting image from Firebase Storage: " + exception.getMessage());
                });
            }
        }


        if (newPhotoUrls.isEmpty()) {
            Toast.makeText(requireContext(), "Please select at least one photo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!title() || !description()) {
            // handle validation errors
            return;
        }


        // Get a reference to the publication in the database
        DatabaseReference publicationRef = databaseReference.getDatabase().getReference("publications/" + publication.getPublication_id());
        DatabaseReference blogRef = databaseReference.getDatabase().getReference("blogs/" + publication.getBlog().getProfile().getProfile_id());

        // Retrieve the publication from the database
        publicationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if the publication exists in the database
                if (snapshot.exists()) {
                    // Get the Publication object from the snapshot
                    Publication publication = snapshot.getValue(Publication.class);

                    // Set the attributes of the Publication object
                    assert publication != null;
                    publication.setTitle(title);
                    publication.setDescription(description);
                    publication.setPhotos(photoUrls);

                    // Update the publication in the database
                    publicationRef.setValue(publication);

                    // Show a success message
                    Toast.makeText(getContext(), "Publication updated successfully", Toast.LENGTH_SHORT).show();
                    uploadPhotos(uriList, publication.getPublication_id(), publication, snapshot, blogRef);
                } else {
                    // Handle the case where the publication does not exist in the database
                    Toast.makeText(getContext(), "Publication not found", Toast.LENGTH_SHORT).show();
                    throw new NullPointerException();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database errors
                Log.e(TAG, "Error retrieving publication from Firebase Realtime Database: " + error.getMessage());
            }
        });
    }

    /**
     * We create a new publication
     */

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
        Publication newPublication = new Publication();

        // Set the attributes of the Publication object
        String publicationId = databaseReference.child("publications").push().getKey();
        newPublication.setPublication_id(publicationId);
        newPublication.setTitle(title);
        newPublication.setDescription(description);
        newPublication.setDatePublished(formattedDate);
        newPublication.setBlog(blog);
        newPublication.setPlacemarkID(placemarkName);
        newPublication.setLikes(new ArrayList<>());
        newPublication.setComments(new ArrayList<>());


        // Get a reference to the blog in the database
        DatabaseReference blogRef = databaseReference.getDatabase().getReference("blogs/" + blog.getProfile().getProfile_id());

        // Retrieve the blog from the database
        blogRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if the blog exists in the database
                if (snapshot.exists()) {
                    uploadPhotos(uriList, publicationId, newPublication, snapshot, blogRef);

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


    /**
     * This method is used only to upload a new publication photos in my firabase storage
     */

    public void uploadPhotos(List<Uri> uris, String publicationId, Publication publication, DataSnapshot snapshot, DatabaseReference blogRef) {
        // Get the Firebase Storage reference with your bucket name
        FirebaseStorage storage = FirebaseStorage.getInstance();
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

                            // Get the blog object from the database
                            Blog existingBlog = snapshot.getValue(Blog.class);

                            // Save the updated blog object to Firebase Realtime Database
                            blogRef.setValue(existingBlog);

                            // Update the existing blog object with the new publication
                            assert existingBlog != null;

                            existingBlog.addPublication(publication);
                            // Update the publication object in the publications array of the blog with the correct ID
                            int index = existingBlog.getPublications().indexOf(publication);
                            if (index != -1) {
                                existingBlog.getPublications().get(index).setPublication_id(publicationId);
                            }

//                            newPublication.setPhotos(publication.getPhotos());


                        });
                    })
                    .addOnFailureListener(exception -> {
                        // Handle errors
                        Log.e(TAG, "Error uploading image to Firebase Storage: " + exception.getMessage());
                    });
        }

        //Navigate to parent fragment
        goToParentFragment();
    }

    /**
     * This code is used to navigate to the parent fragment
     */

    public void goToParentFragment() {
        //Add publication here
        // In the child fragment:
        // Get the parent FragmentManager instance
        FragmentManager parentFragmentManager = getParentFragmentManager();

        // Define enter and exit animations for the fragment transition
        int enterAnim = R.anim.slide_in_right;
        int exitAnim = R.anim.slide_out_left;

        Fragment fragment;

        if (!isNews) {
            fragment = new FragmentBlog();
        } else {
            fragment = new FragmentNews();
        }

        // Create and set the fragment transition animation object
        Transition fragmentTransition = new Slide(Gravity.START);
        fragmentTransition.setDuration(1000);
        FragmentTransaction fragmentTransaction = parentFragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.setCustomAnimations(enterAnim, exitAnim);
        fragmentTransaction.replace(R.id.fragment_add_publication, fragment);
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