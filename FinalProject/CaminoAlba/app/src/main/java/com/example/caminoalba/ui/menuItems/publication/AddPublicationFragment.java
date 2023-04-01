package com.example.caminoalba.ui.menuItems.publication;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.example.caminoalba.interfaces.IAPIservice;
import com.example.caminoalba.models.Blog;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.dto.Publication;
import com.example.caminoalba.ui.menuItems.publication.recyclers.RecyclerAdapterPhotos;
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
    private List<Blog> blogs;
    private Profile profile;
    private String description, title;
    // ------ Para obtener el recyclerview    -------
    private RecyclerAdapterPhotos adapter;
    private RecyclerView recyclerView;
    // ------ Otras referencias    -------
    private List<Uri> uriList;

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
        blogs = new ArrayList<>();
        Blog blog = new Blog();
        uriList = new ArrayList<>();
        Publication publication = new Publication();
        recyclerView = view.findViewById(R.id.rvPhotoGrid);
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
        String formattedDate = datePublished.format(formatter);
        tvTimeDisplayed.setText(formattedDate);
        Picasso.get().load(profile.getPhoto()).into(imageView);


        addPhotos();

//        service.getBlogsFromRest(new Service.APICallback() {
//            @Override
//            public void onSuccess() {
//                blogs = service.getBlogs();
//                Blog blog1 = new Blog();
//
//                for (int i = 0; i < blogs.size(); i++) {
//                    if (blogs.get(i).getBlog_id() == profile.getProfile_id()) {
//                        blog1 = blogs.get(i);
//                    }
//                }
//
//                blog = blog1;
//
//                if (profile.getPhoto() != null) {
//                    imageView.setImageURI(Uri.parse(profile.getPhoto()));
//                }
//
//
//                publication.setBlog(blog);
//
//                btnAddPublication.setOnClickListener(v -> {
//
//                    if (uriList.isEmpty()) {
//                        Toast.makeText(requireContext(), "Please select at least one photo", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    if (!title() || !description()) {
//                        // handle validation errors
//                        return;
//                    }
//
//                    List<Publication> publicationList = new ArrayList<>();
//                    publicationList.add(publication);
//                    blog.setPublications(publicationList);
//                    List<String> photos = new ArrayList<>();
//                    for (Uri uri : uriList) {
//                        photos.add(uri.toString());
//                    }
//                    publication.setPhotos(photos);
//                    publication.setTitle(title);
//                    publication.setDescription(description);
//                    publication.setDatePublished(formattedDate);
//
//                    Call<Publication> call = iapiService.addPublication((long) blog.getBlog_id(), publication);
//                    call.enqueue(new Callback<Publication>() {
//
//                        @Override
//                        public void onResponse(Call<Publication> call, Response<Publication> response) {
//                            Toast.makeText(getContext(), "Publication has been sent sucessfully", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onFailure(Call<Publication> call, Throwable t) {
//
//                        }
//                    });
//                });
//
//            }
//
//            @Override
//            public void onFailure(String error) {
//                Toast.makeText(getContext(), "Publication falied", Toast.LENGTH_SHORT).show();
//            }
//        });


    }


    //Desde el boton puedo subir varias fotos y guardarlos en un imageview para despues
    //representarlo en el recycler view.

    public void addPhotos() {
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


//    The updateUriList() method is called from the onActivityResult() method to update the uriList variable.
//    The addPhotos() method is modified to remove the check for the maximum number of photos, as this is now handled in the updateUriList()
    public void updateUriList(Uri uri) {
        if (uriList.size() < MAX_PHOTOS) {
            uriList.add(uri);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(requireContext(), "Maximum number of photos reached", Toast.LENGTH_SHORT).show();
        }
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