package com.example.caminoalba.ui.others;

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
import com.example.caminoalba.models.Blog;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.dto.Publication;
import com.example.caminoalba.services.Service;
import com.example.caminoalba.ui.menuItems.recyclers.RecyclerAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AddPublicationFragment extends Fragment {

    private static final int MAX_PHOTOS = 5; // maximum number of photos allowed

    // ------ Vistas   -------
    private TextView tvName, tvTime;
    private EditText etTitle, etDescription;
    private ImageView imageView;
    private Button btnImage;
    // ------ Para obtener el blog y publicacion  -------
    private List<Blog> blogs;
    private Profile profile;
    private Blog blog;
    private List<Publication> publicationList;
    // ------ Para obtener el recyclerview    -------
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    // ------ Otras referencias    -------
    private Service service;
    private List<Uri> uriList;
    private SharedPreferences preferences;


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
        tvName = view.findViewById(R.id.authorName);
        etTitle = view.findViewById(R.id.etTitleField);
        tvTime = view.findViewById(R.id.tvTimeDisplayed);
        etDescription = view.findViewById(R.id.etDescriptionField);
        btnImage = view.findViewById(R.id.addPhotoButton);
        imageView = view.findViewById(R.id.authorPhoto);
        // ------ Inicializamos variables  -------
        uriList = new ArrayList<>();
        service = new Service();

        recyclerView = view.findViewById(R.id.rvPhotoGrid);
//        recyclerView = new RecyclerView(requireContext());
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        getBlog();
        // ------  RecyclerView   -------
        addPhotos();

    }

    public void showAuthorInfo() {
        tvName.setText(profile.getFirstName());
        if (profile.getPhoto() != null) {
            imageView.setImageURI(Uri.parse(profile.getPhoto()));
        }
    }


    public void getBlog() {
        Gson gson = new Gson();
        String profileStr = preferences.getString("profile", "");
        profile = gson.fromJson(profileStr, Profile.class);
        System.out.println("esto es profile desde add " + profile);
        showAuthorInfo();
        blogs = new ArrayList<>();
        blog = new Blog();
        service.getBlogsFromRest(new Service.APICallback() {
            @Override
            public void onSuccess() {
                blogs = service.getBlogs();
                System.out.println("esto es blogs " + blogs);
                for (int i = 0; i < blogs.size(); i++) {
                    if (blogs.get(i).getBlog_id() == profile.getProfile_id()) {
                        blog = blogs.get(i);
                    }
                }
                System.out.println("esto es blog seleccionado " + blog);
            }

            @Override
            public void onFailure(String error) {
                System.out.println("esto es blog seleccionado desde fallo " + blog);
            }
        });

    }

    //Desde el boton puedo subir varias fotos y guardarlos en un imageview para despues
    //representarlo en el recycler view.

    public void addPhotos() {
        btnImage.setOnClickListener(v -> {
            if (uriList.size() >= MAX_PHOTOS) {
                Toast.makeText(requireContext(), "Maximum number of photos reached", Toast.LENGTH_SHORT).show();
                return;
            }
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
                    uriList.add(uri);
                }
            } else {
                // single photo selected
                Uri uri = data.getData();
                uriList.add(uri);
            }
        }
        System.out.println("Selected URIs: " + uriList);
        System.out.println("Number of URIs: " + uriList.size());
        adapter = new RecyclerAdapter(uriList);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 4));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}