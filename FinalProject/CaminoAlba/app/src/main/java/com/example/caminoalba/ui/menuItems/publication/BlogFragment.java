package com.example.caminoalba.ui.menuItems.publication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.example.caminoalba.models.Blog;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.Publication;
import com.example.caminoalba.models.User;
import com.example.caminoalba.ui.menuItems.publication.recyclers.RecyclerPublicationAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class BlogFragment extends Fragment {

    private EditText etSearchBar;;
    private FrameLayout frameLayout;
    private SharedPreferences preferences;
    private User user;
    private Blog blog;
    private Profile profile;
    private ImageView imgHome, imgBlog, imgAddPublication;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ------ Inicializamos vistas   -------
        etSearchBar = view.findViewById(R.id.etSearch_bar);
        frameLayout = view.findViewById(R.id.fragment_blog);
        imgHome = view.findViewById(R.id.imgHome);
        imgBlog = view.findViewById(R.id.imgBlog);
        imgAddPublication = view.findViewById(R.id.imgAddPublication);
        recyclerView = view.findViewById(R.id.rvPublications);
        // ------ Inicializamos variables  -------
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        user = new User();
        profile = new Profile();
        blog = new Blog();
        Gson gson = new Gson();
        String userStr = preferences.getString("user", "");
        String profileStr = preferences.getString("profile", "");
        user = gson.fromJson(userStr, User.class);
        profile = gson.fromJson(profileStr, Profile.class);
        eventHandler();


    }




    public void getPublications(Blog blog){
        String blogId = blog.getBlog_id(); // replace with actual blog ID

        DatabaseReference blogRef = FirebaseDatabase.getInstance().getReference("blogs").child(blogId).child("publications");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Publication> publications = new ArrayList<>();
                for (DataSnapshot publicationSnapshot : dataSnapshot.getChildren()) {
                    Publication publication = publicationSnapshot.getValue(Publication.class);
                    if (publication != null && publication.getPublication_id().equals(publicationSnapshot.getKey())) {
                        publications.add(publication);
                    }
                }
                // do something with publications list
                RecyclerPublicationAdapter recyclerPublicationAdapter = new RecyclerPublicationAdapter(publications, requireContext());
                recyclerView.setAdapter(recyclerPublicationAdapter);
                recyclerPublicationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // handle error
            }
        };

        blogRef.addListenerForSingleValueEvent(listener);
    }




    public void eventHandler(){
        // Get the current user from FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference blogRef = FirebaseDatabase.getInstance().getReference("blogs").child(uid);
            blogRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    blog = dataSnapshot.getValue(Blog.class);
                    assert blog != null;
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

                     getPublications(blog);


//                    RecyclerPublicationAdapter recyclerPublicationAdapter = new RecyclerPublicationAdapter(blog.getPublications(), requireContext());
//                    recyclerView.setAdapter(recyclerPublicationAdapter);
//                    recyclerPublicationAdapter.notifyDataSetChanged();


                    imgAddPublication.setOnClickListener(v -> {
                        //Create varibles to pass to my child fragment
                        Bundle args = new Bundle();
                        args.putSerializable("profile", profile);
                        args.putSerializable("blog", blog);
                        // Create an instance of the child fragment
                        AddPublicationFragment addPublicationFragment = new AddPublicationFragment();
                        //Pass the args already created to the child fragment
                        addPublicationFragment.setArguments(args);
                        // Begin a new FragmentTransaction using the getChildFragmentManager() method
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        // Add the child fragment to the transaction and specify a container view ID in the parent layout
                        transaction.add(R.id.fragment_blog, addPublicationFragment);
                        transaction.addToBackStack(null); // Add the fragment to the back stack
                        transaction.commit();

                    });

                    imgBlog.setOnClickListener(v -> {

                    });

                    imgHome.setOnClickListener(v -> {

                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Couldn't get the profile", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }









}