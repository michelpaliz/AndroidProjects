package com.example.caminoalba.ui.menuItems.publication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FragmentBlog extends Fragment implements FragmentMap.OnDataPass {

    private String placemarkName;
    private Blog blog;
    private Profile profile;
    private TextView tvPathName, tvRuta;
    private ImageView btnAddPublication;
    private RecyclerView recyclerView;
    private TextView tvMessage, tvTitle;
    private Context context;
    private boolean showPublicationByUser = false;
    private boolean isAdmin = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = requireContext();
        return inflater.inflate(R.layout.fragment_blog, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ------ Init Views   -------
        tvPathName = view.findViewById(R.id.tvPathName);
        tvRuta = view.findViewById(R.id.tvRuta);
        recyclerView = view.findViewById(R.id.rvPublications);
        tvMessage = view.findViewById(R.id.tvMessage);
        tvTitle = view.findViewById(R.id.tvTitle);
        LinearLayout linearLayoutPath = view.findViewById(R.id.linearLayout_path);
        LinearLayout footerMenu = view.findViewById(R.id.footer_menu);
        ImageView imgPoints = view.findViewById(R.id.imgPoints);
        ImageView imgMap = view.findViewById(R.id.imgMap);
        btnAddPublication = view.findViewById(R.id.imgAddPublication);
        // ------ Init Variables  -------
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        profile = new Profile();
        blog = new Blog();
        Gson gson = new Gson();
        String userStr = preferences.getString("user", "");
        String profileStr = preferences.getString("profile", "");
        User user = gson.fromJson(userStr, User.class);
        profile = gson.fromJson(profileStr, Profile.class);
        // ------ Starting with the logic  -------
        btnAddPublication.setVisibility(View.GONE);
        tvTitle.setText("PUBLICACIONES");
        tvMessage.setText("Please go to the map section to set your current location.");
        if (getArguments() != null) {
            isAdmin = getArguments().getBoolean("isAdmin", false);
            showPublicationByUser = getArguments().getBoolean("userlist", false);
        }

        if (isAdmin) {
            tvTitle.setText("NOTICIAS");
            footerMenu.setVisibility(View.GONE);
            linearLayoutPath.setVisibility(View.GONE);
            tvMessage.setText("Here you can see the lates news about Camino del Alba");
            getCurrentBlog();
            if (user.getType().equalsIgnoreCase("admin")) {
                btnAddPublication.setVisibility(View.VISIBLE);
            }
        }

        imgMap.setOnClickListener(v -> {
            // Create an instance of the child fragment
            FragmentMap fragmentMap = new FragmentMap();
            // Begin a new FragmentTransaction using the getChildFragmentManager() method
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            // Add the child fragment to the transaction and specify a container view ID in the parent layout
            transaction.add(R.id.fragment_blog, fragmentMap);
            transaction.addToBackStack(null); // Add the fragment to the back stack
            transaction.commit();
        });

        //When we enter again from the showpublicationbyuser fragment we do this
        if (showPublicationByUser) {
            tvMessage.setText("Here you can see your publications that you have, you can remove them");
            tvPathName.setVisibility(View.GONE);
            tvRuta.setVisibility(View.GONE);
//            ivDeletePublication.setVisibility(View.VISIBLE);
            getCurrentBlog();
        }

        imgPoints.setOnClickListener(v -> {
            // Create an instance of the child fragment
            FragmentUserPublications fragmentUserPublications = new FragmentUserPublications();
            // Begin a new FragmentTransaction using the getChildFragmentManager() method
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            // Add the child fragment to the transaction and specify a container view ID in the parent layout
            transaction.replace(R.id.fragment_blog, fragmentUserPublications);
            transaction.addToBackStack(null); // Add the fragment to the back stack
            transaction.commit();
        });

    }


    @Override
    public void onDataPass(String placemarkName, boolean isEnabled) {
        this.placemarkName = placemarkName;
        if (isEnabled) {
            tvMessage.setVisibility(View.GONE);
            tvPathName.setText(placemarkName);
            btnAddPublication.setVisibility(View.VISIBLE);
            getCurrentBlog();
        } else {
            tvMessage.setText("Make sure to be less than 50 meters in one placemark in order to see the publications");
            tvRuta.setVisibility(View.GONE);
            tvPathName.setVisibility(View.GONE);
        }
    }

    public void getPublications() {

        DatabaseReference publicationsRef = FirebaseDatabase.getInstance().getReference().child("publications");

        publicationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Publication> publicationsList = new ArrayList<>();

                for (DataSnapshot publicationSnapshot : dataSnapshot.getChildren()) {
                    Publication publication = publicationSnapshot.getValue(Publication.class);
                    publicationsList.add(publication);
                    // Call updatePublicationProfilePhoto() for this publication
                    assert publication != null;
                }

                // Here, you can do something with the list of publications.
                List<Publication> publicationsById = new ArrayList<>();
                RecyclerPublicationAdapter recyclerPublicationAdapter;
                Blog blog1 = new Blog();
                blog1.setProfile(profile);

                if (!showPublicationByUser) {
                    recyclerPublicationAdapter = new RecyclerPublicationAdapter(publicationsList, profile, context);
                } else {
                    assert profile != null;
                    for (Publication publication : publicationsList) {
                        if (publication.getBlog().getBlog_id().equalsIgnoreCase(profile.getProfile_id())) {
                            publication.getBlog().setProfile(blog1.getProfile());
                            publicationsById.add(publication);
                        }
                    }

                    recyclerPublicationAdapter = new RecyclerPublicationAdapter(publicationsById, profile, context);

                }

                List<Publication> publicationsFound = new ArrayList<>();

                if (placemarkName != null && !showPublicationByUser) {
                    for (Publication publication : publicationsList) {
                        if (placemarkName.equalsIgnoreCase(publication.getPlacemarkID())) {
                            publication.getBlog().setProfile(blog1.getProfile());
                            publicationsFound.add(publication);
                        }
                    }
                    recyclerPublicationAdapter = new RecyclerPublicationAdapter(publicationsFound, profile, context);

                }

                //We get the reference from our interface and the remove the image

                recyclerPublicationAdapter.setOnPublicationClickListener((publicationId, remove) -> {
                    DatabaseReference publicationRef = FirebaseDatabase.getInstance().getReference("publications").child(publicationId);
                    publicationRef.removeValue()
                            .addOnSuccessListener(aVoid -> Toast.makeText(context, "Publication deleted successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(context, "Error deleting publication", Toast.LENGTH_SHORT).show());


                });

                recyclerView.setAdapter(recyclerPublicationAdapter);
                recyclerPublicationAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that may occur.
            }
        });
    }


    public void getCurrentBlog() {
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
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//                    layoutManager.setReverseLayout(true);
                    recyclerView.setLayoutManager(layoutManager);
                    getPublications();
                    addPublicationFragment(blog);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Couldn't get the profile", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void addPublicationFragment(Blog blog) {
        btnAddPublication.setOnClickListener(v -> {
            //Create varibles to pass to my child fragment
            Bundle args = new Bundle();
            blog.setProfile(profile);
            args.putSerializable("blog", blog);
            args.putString("placemark", placemarkName);
            args.putBoolean("isAdmin", isAdmin);
            // Create an instance of the child fragment
            FragmentActionPublication fragmentActionPublication = new FragmentActionPublication();
            //Pass the args already created to the child fragment
            fragmentActionPublication.setArguments(args);
            // Begin a new FragmentTransaction using the getChildFragmentManager() method
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            // Add the child fragment to the transaction and specify a container view ID in the parent layout
            transaction.add(R.id.fragment_blog, fragmentActionPublication);
            transaction.addToBackStack(null); // Add the fragment to the back stack
            transaction.commit();

        });
    }
}