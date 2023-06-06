package com.example.caminoalba.ui.menuItems.publication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.example.caminoalba.interfaces.ChildToParentInterface;
import com.example.caminoalba.models.Blog;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.Publication;
import com.example.caminoalba.models.User;
import com.example.caminoalba.ui.menuItems.publication.recyclers.RecyclerAdapterPathItem;
import com.example.caminoalba.ui.menuItems.publication.recyclers.RecyclerAdapterPublication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentPublication extends Fragment implements FragmentMap.OnDataPass, RecyclerAdapterPathItem.OnItemClickListener, ChildToParentInterface {

    private String placemarkName;
    private Blog blog;
    private Profile profile;
    private TextView tvPathName, tvRuta;
    private ImageView btnAddPublication;
    private RecyclerView publicationRecyclerview, pathItemRecyclerview;
    private TextView tvMessage, tvTitle, tvNoBadgesUnlocked;
    private Context context;
    private RecyclerAdapterPublication recyclerAdapterPublication;
    private boolean showPublicationByUser = false, isEnabled;
    private boolean isNews, comesFromUserListUserFragment = false;
    private String selectedItem;
    private Bundle args;


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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("placemarkName", placemarkName);
        outState.putBoolean("isEnabled", isEnabled);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ------ Init Views   -------
        tvPathName = view.findViewById(R.id.tvPathName);
        tvRuta = view.findViewById(R.id.tvRuta);
        publicationRecyclerview = view.findViewById(R.id.rvPublications);
        pathItemRecyclerview = view.findViewById(R.id.rvPathsUnlocked);
        tvMessage = view.findViewById(R.id.tvMessage);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvNoBadgesUnlocked = view.findViewById(R.id.tvNoBadgesMessage);
        args = new Bundle();

        LinearLayout linearLayoutPath = view.findViewById(R.id.linearLayout_path);
        LinearLayout footerMenu = view.findViewById(R.id.footer_menu);

        ImageView imgUserList = view.findViewById(R.id.imgPoints);
        ImageView imgHome = view.findViewById(R.id.imgHome);
        ImageView imgMap = view.findViewById(R.id.imgMap);
        btnAddPublication = view.findViewById(R.id.imgAddPublication);

        //----- Image color ------
        int color = ContextCompat.getColor(requireContext(), R.color.darkpink); // Replace with your desired color resource ID
        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_home_24); // Replace with your vector image resource ID
        imgHome.setImageDrawable(drawable);
        imgHome.setColorFilter(color, PorterDuff.Mode.SRC_IN);


        // ------ Init Variables  -------
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        profile = new Profile();
        Gson gson = new Gson();
        String userStr = preferences.getString("user", "");
        String profileStr = preferences.getString("profile", "");

        User user = gson.fromJson(userStr, User.class);
        profile = gson.fromJson(profileStr, Profile.class);

        //Here we hide the message for the home fragment
        linearLayoutPath.setVisibility(View.GONE);

        // ------ Starting with the logic  -------
        btnAddPublication.setVisibility(View.GONE);
        tvTitle.setText(getText(R.string.publication).toString().toUpperCase());
        if (getArguments() != null) {
            isNews = getArguments().getBoolean("isNews", false);
            showPublicationByUser = getArguments().getBoolean("userlist", false);
            comesFromUserListUserFragment = getArguments().getBoolean("comesFromUserList", false);
        }

        tvRuta.setVisibility(View.GONE);

        //Now I need to charge my recyclerview for my unlocked paths
        tvNoBadgesUnlocked.setVisibility(View.GONE);
        if (!isNews || !showPublicationByUser) {
            if (profile.getBadges() != null) {
                if (!profile.getBadges().isEmpty()) {
                    pathItemRecyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    RecyclerAdapterPathItem adapter = new RecyclerAdapterPathItem(profile.getBadges(), context);
                    adapter.setOnItemClickListener(this);
                    pathItemRecyclerview.setAdapter(adapter);
                }
            } else {
                pathItemRecyclerview.setVisibility(View.GONE);
                tvNoBadgesUnlocked.setVisibility(View.VISIBLE);
                tvNoBadgesUnlocked.setText(getText(R.string.noBadgesFound));
            }
        }

        if (isNews) {
            tvNoBadgesUnlocked.setVisibility(View.GONE);
            pathItemRecyclerview.setVisibility(View.GONE);
            tvTitle.setText(getText(R.string.news).toString().toUpperCase());
            footerMenu.setVisibility(View.GONE);
            linearLayoutPath.setVisibility(View.GONE);
            tvMessage.setText(getText(R.string.newsInformation));
            getCurrentBlog();

            if (user != null && user.getType() != null) {
                if (user.getType().equalsIgnoreCase("admin")) {
                    tvMessage.setText(getText(R.string.admin_news));
                    btnAddPublication.setVisibility(View.VISIBLE);
                }
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


        if (comesFromUserListUserFragment) {
            tvNoBadgesUnlocked.setVisibility(View.GONE);
            imgHome.setOnClickListener(v -> {
                FragmentManager fragmentManager = getChildFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    // If there are fragments in the back stack, pop the topmost fragment
                    fragmentManager.popBackStack();
                } else {
                    // If there are no fragments in the back stack, navigate to a specific fragment
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_blog);
                    navController.navigate(R.id.blogFragment);
                }

            });
        }


        //When we enter again from the showpublicationbyuser fragment we do this
        if (showPublicationByUser) {
            // Remove the color filter to revert to the original color
            imgHome.setColorFilter(null);
            int color1 = ContextCompat.getColor(requireContext(), R.color.darkpink); // Replace with your desired color resource ID
            Drawable drawable1 = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_card_travel_24); // Replace with your vector image resource ID
            imgUserList.setImageDrawable(drawable1);
            imgUserList.setColorFilter(color1, PorterDuff.Mode.SRC_IN);
            pathItemRecyclerview.setVisibility(View.GONE);

            if (profile.getUser().getType() != null) {
                if (profile.getUser().getType().equalsIgnoreCase("admin")) {
                    tvMessage.setText(getText(R.string.ownPhotosMessageForAdmin));
                } else {
                    tvMessage.setText(getText(R.string.ownPhotosMessage));
                }
            }
            tvPathName.setVisibility(View.GONE);
            tvRuta.setVisibility(View.GONE);
            getCurrentBlog();
        }

        imgUserList.setOnClickListener(v -> {
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
     * @param placemarkName the id of the placemark that the user is
     * @param isEnabled     this will enable the user to add publications for the id of the placemark
     */
    @Override
    public void onDataPass(String placemarkName, boolean isEnabled) {
//        this.placemarkName = placemarkName;
//        if (isEnabled) {
//            tvMessage.setVisibility(View.GONE);
//            tvPathName.setText(placemarkName);
//            btnAddPublication.setVisibility(View.VISIBLE);
//            getCurrentBlog();
//
//        } else {
////            tvMessage.setText(getText(R.string.beSureToBeLess50m));
//            tvRuta.setVisibility(View.GONE);
//            tvPathName.setVisibility(View.GONE);
//        }
    }

    /**
     * Show publications for the newsFragment/blogFragment
     */
    public void showPublications() {

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
                Blog blog1 = new Blog();
                blog1.setProfile(profile);


                //1. In the first condition we only show the admin publications for the news fragment.
                if (!showPublicationByUser) {
                    List<Publication> adminPublications = new ArrayList<>();
                    for (Publication publication : publicationsList) {
                        if (publication.getBlog().getProfile().getUser().getType().equalsIgnoreCase("admin") &&
                                publication.isNews()) {
                            adminPublications.add(publication);
                        }
                    }
                    recyclerAdapterPublication = new RecyclerAdapterPublication(sortPublicationsByDate(adminPublications), profile, context, isNews, showPublicationByUser);
                } else {  //2. In the second condition we only take the profile's publications
                    assert profile != null;
                    for (Publication publication : publicationsList) {
                        if (publication.getBlog().getProfile().getProfile_id().equalsIgnoreCase(profile.getProfile_id())) {
                            publication.getBlog().setProfile(blog1.getProfile());
                            publicationsById.add(publication);
                        }
                    }

                    recyclerAdapterPublication = new RecyclerAdapterPublication(sortPublicationsByDate(publicationsById), profile, context, isNews, showPublicationByUser);

                }


                //3. At third condition we show only the publications that have an specific point in the fragment
                List<Publication> publicationsFound = new ArrayList<>();

                if (placemarkName != null && !showPublicationByUser) {
                    for (Publication publication : publicationsList) {
                        if (placemarkName.equalsIgnoreCase(publication.getPlacemarkID())) {
//                            publication.getBlog().setProfile(blog1.getProfile());
                            publicationsFound.add(publication);
                        }
                    }
                    recyclerAdapterPublication = new RecyclerAdapterPublication(sortPublicationsByDate(publicationsFound), profile, context, isNews, showPublicationByUser);
                }

                //We get the reference from our interface and the remove the image
                recyclerAdapterPublication.setOnPublicationClickListener((publicationId, remove) -> {

                    if (profile.getUser().getType().equalsIgnoreCase("admin")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Confirm Deletion");
                        builder.setMessage("Are you sure you want to delete this publication?");
                        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                            DatabaseReference publicationRef = FirebaseDatabase.getInstance().getReference("publications").child(publicationId);
                            publicationRef.removeValue()
                                    .addOnSuccessListener(aVoid -> Toast.makeText(context, R.string.publication_deleted_successfully, Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(context, R.string.publication_deletec_unsuccessfully, Toast.LENGTH_SHORT).show());
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();

                            // Get a reference to the folder for the publication's photos
                            String folderPath = "publications/" + publicationId;
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


                publicationRecyclerview.setAdapter(recyclerAdapterPublication);
                recyclerAdapterPublication.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that may occur.
            }
        });
    }


    /**
     *
     * @param publications
     * @return publications sorted
     */

    private List<Publication> sortPublicationsByDate(List<Publication> publications) {
        // Sort the publications by date in ascending order
        publications.sort(new Comparator<Publication>() {
            final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            @Override
            public int compare(Publication p1, Publication p2) {
                try {
                    Date date1 = dateFormat.parse(p1.getDatePublished());
                    Date date2 = dateFormat.parse(p2.getDatePublished());
                    assert date1 != null;
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        // Reverse the order of the sorted list to display the newest publications first
        Collections.reverse(publications);

        return publications;
    }


    /**
     * In order to upload a new publication we need to know the blog entity
     */
    public void getCurrentBlog() {
        showPublications();
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
                    publicationRecyclerview.setHasFixedSize(true);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//                    layoutManager.setReverseLayout(true);
                    publicationRecyclerview.setLayoutManager(layoutManager);
                    goToActionPublication(blog);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Couldn't get the profile", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void goToActionPublication(Blog blog) {
        btnAddPublication.setOnClickListener(v -> {
            //Create varibles to pass to my child fragment
//            Bundle args = new Bundle();
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
            transaction.replace(R.id.fragment_blog, fragmentActionPublication);
            transaction.addToBackStack(null); // Add the fragment to the back stack
            transaction.commit();

        });
    }


    // Call this method after deleting a publication to update the RecyclerView
    private void updateRecyclerView(List<Publication> publications, String item) {

        RecyclerAdapterPublication recyclerAdapterPublication = new RecyclerAdapterPublication(sortPublicationsByDate(publications), profile, context, false, false);

        recyclerAdapterPublication.setOnPublicationClickListener((publicationId, remove) -> {
            if (profile.getUser().getType().equalsIgnoreCase("admin")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Deletion");
                builder.setMessage(R.string.confirmation_question_delete_publication);
                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    DatabaseReference publicationRef = FirebaseDatabase.getInstance().getReference("publications").child(publicationId);
                    publicationRef.removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(context, R.string.publication_deleted_successfully, Toast.LENGTH_SHORT).show();
                                // Refresh the RecyclerView after successful deletion
                                showPublicationsByPath(item);
                            })
                            .addOnFailureListener(e -> Toast.makeText(context, R.string.publication_deletec_unsuccessfully, Toast.LENGTH_SHORT).show());

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();

                    // Get a reference to the folder for the publication's photos
                    String folderPath = "publications/" + publicationId;
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


        publicationRecyclerview.setAdapter(recyclerAdapterPublication);
        publicationRecyclerview.setLayoutManager(new LinearLayoutManager(context));
        recyclerAdapterPublication.notifyDataSetChanged();
    }


    public void showPublicationsByPath(String item) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference publicationsRef = database.getReference("publications");
        Query query = publicationsRef.orderByChild("placemarkID").equalTo(item);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the list before adding new publications
                List<Publication> filteredPublications = new ArrayList<>();

                // Iterate through the retrieved publications
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Publication publication = snapshot.getValue(Publication.class);

                    // Compare the placemarkID attribute with the name passed as an argument
                    assert publication != null;
                    if (publication.getPlacemarkID().equals(item)) {
                        // Add the matching publication to the list
                        filteredPublications.add(publication);
                    }
                }

                RecyclerAdapterPublication recyclerAdapterPublication = new RecyclerAdapterPublication(sortPublicationsByDate(filteredPublications), profile, context, false, false);
                publicationRecyclerview.setAdapter(recyclerAdapterPublication);
                publicationRecyclerview.setLayoutManager(new LinearLayoutManager(context));
                recyclerAdapterPublication.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the data retrieval
                // ...
            }
        });

    }

    @Override
    public void onItemClick(String item) {
        isNews = false;
        showPublicationByUser = false;
        btnAddPublication.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference publicationsRef = database.getReference("publications");
        Query query = publicationsRef.orderByChild("placemarkID").equalTo(item);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the list before adding new publications
                List<Publication> filteredPublications = new ArrayList<>();

                // Iterate through the retrieved publications
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Publication publication = snapshot.getValue(Publication.class);

                    // Compare the placemarkID attribute with the name passed as an argument
                    assert publication != null;
                    if (publication.getPlacemarkID() != null && publication.getPlacemarkID().equals(item)) {
                        // Add the matching publication to the list
                        filteredPublications.add(publication);
                    }

                }

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
                            publicationRecyclerview.setHasFixedSize(true);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            publicationRecyclerview.setLayoutManager(layoutManager);

                            // Call the method to update the RecyclerView with the filtered publications
                            updateRecyclerView(filteredPublications, item);

                            btnAddPublication.setOnClickListener(v -> {
                                // Create variables to pass to my child fragment
                                blog.setProfile(profile);
                                args.putSerializable("blog", blog);
                                args.putString("placemark", item);
                                args.putBoolean("isNews", false);
                                args.putString("selectedItem", item);

                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                FragmentActionPublication fragmentActionPublication = new FragmentActionPublication();
                                fragmentActionPublication.setArguments(args);
                                fragmentActionPublication.setChildToParentInterface(FragmentPublication.this); // Set the parent fragment as the listener
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.add(R.id.fragment_blog, fragmentActionPublication);
                                transaction.addToBackStack(null);
                                transaction.commit();

                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getContext(), "Couldn't get the profile", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the data retrieval
                // ...
            }
        });
    }


    @Override
    public void onItemClicked(String item) {
        selectedItem = item;
        onItemClick(item);
    }
}