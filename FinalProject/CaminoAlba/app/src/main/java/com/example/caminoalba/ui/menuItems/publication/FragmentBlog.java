package com.example.caminoalba.ui.menuItems.publication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private boolean isNews;

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
        Gson gson = new Gson();
        String userStr = preferences.getString("user", "");
        String profileStr = preferences.getString("profile", "");
        User user = gson.fromJson(userStr, User.class);
        profile = gson.fromJson(profileStr, Profile.class);
        // ------ Starting with the logic  -------
        btnAddPublication.setVisibility(View.GONE);
        tvTitle.setText(getText(R.string.publication).toString().toUpperCase());
        tvMessage.setText(getText(R.string.setUpLocation).toString().toUpperCase());
        if (getArguments() != null) {
            isNews = getArguments().getBoolean("isNews", false);
            showPublicationByUser = getArguments().getBoolean("userlist", false);
        }

        if (isNews) {
            tvTitle.setText(getText(R.string.news).toString().toUpperCase());
            footerMenu.setVisibility(View.GONE);
            linearLayoutPath.setVisibility(View.GONE);
            tvMessage.setText(getText(R.string.newsInformation));
            getCurrentBlog();
            if (user.getType().equalsIgnoreCase("admin")) {
                tvMessage.setText(getText(R.string.admin_news));
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
            tvMessage.setText(getText(R.string.ownPhotosMessage));
            tvPathName.setVisibility(View.GONE);
            tvRuta.setVisibility(View.GONE);
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

    /**
     *
     * @param placemarkName the id of the placemark that the user is
     * @param isEnabled this will enable the user to add publications for the id of the placemark
     */
    @Override
    public void onDataPass(String placemarkName, boolean isEnabled) {
        this.placemarkName = placemarkName;
        if (isEnabled) {
            tvMessage.setVisibility(View.GONE);
            tvPathName.setText(placemarkName);
            btnAddPublication.setVisibility(View.VISIBLE);
            getCurrentBlog();

        } else {
            tvMessage.setText(getText(R.string.beSureToBeLess50m));
            tvRuta.setVisibility(View.GONE);
            tvPathName.setVisibility(View.GONE);
        }
    }

    /**
     * Show publications for the newsFragment/blogFragment
     */
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

                //1. In the first condition we only show the admin publications for the news fragment.
                if (!showPublicationByUser) {
                    List<Publication> adminPublications = new ArrayList<>();
                    for (Publication publication : publicationsList) {
                        if (publication.getBlog().getProfile().getUser().getType().equalsIgnoreCase("admin")) {
                            adminPublications.add(publication);
                        }
                    }
                    recyclerPublicationAdapter = new RecyclerPublicationAdapter(adminPublications, profile, context, isNews);
                } else {  //2. In the second condition we only take the profile's publications
                    assert profile != null;
                    for (Publication publication : publicationsList) {
                        if (publication.getBlog().getProfile().getProfile_id().equalsIgnoreCase(profile.getProfile_id())) {
                            publication.getBlog().setProfile(blog1.getProfile());
                            publicationsById.add(publication);
                        }
                    }

                    recyclerPublicationAdapter = new RecyclerPublicationAdapter(publicationsById, profile, context, isNews);

                }

                //3. At third condition we show only the publications that have an specific point in the fragment
                List<Publication> publicationsFound = new ArrayList<>();

                if (placemarkName != null && !showPublicationByUser) {
                    for (Publication publication : publicationsList) {
                        if (placemarkName.equalsIgnoreCase(publication.getPlacemarkID())) {
                            publication.getBlog().setProfile(blog1.getProfile());
                            publicationsFound.add(publication);
                        }
                    }
                    recyclerPublicationAdapter = new RecyclerPublicationAdapter(publicationsFound, profile, context, isNews);

                }

                //We get the reference from our interface and the remove the image
                recyclerPublicationAdapter.setOnPublicationClickListener((publicationId, remove) -> {

                    if (profile.getUser().getType().equalsIgnoreCase("admin")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Confirm Deletion");
                        builder.setMessage("Are you sure you want to delete this publication?");
                        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                            DatabaseReference publicationRef = FirebaseDatabase.getInstance().getReference("publications").child(publicationId);
                            publicationRef.removeValue()
                                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Publication deleted successfully", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(context, "Error deleting publication", Toast.LENGTH_SHORT).show());
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();

                            // Get a reference to the folder for the publication's photos
                            String folderPath = "publications/" + publicationId ;
                            StorageReference folderRef = storageRef.child(folderPath);

                            // Delete all photos in the folder
                            folderRef.listAll().addOnSuccessListener(listResult -> {
                                List<StorageReference> photos = listResult.getItems();
                                for (StorageReference photo : photos) {
                                    photo.delete();
                                }
                            }).addOnFailureListener(exception -> {
                                // Handle errors
                                Log.e(TAG, "Error deleting photos from Firebase Storage: " + exception.getMessage());
                            });

                            // Delete the folder itself
                            folderRef.delete().addOnSuccessListener(aVoid -> {
                                // Folder deleted successfully
                                Log.d(TAG, "Publication folder deleted from Firebase Storage");
                            }).addOnFailureListener(exception -> {
                                // Handle errors
                                Log.e(TAG, "Error deleting publication folder from Firebase Storage: " + exception.getMessage());
                            });

                        });
                        builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

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
            args.putBoolean("isNews", isNews);

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