package com.example.caminoalba.ui.menuItems.publication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.Publication;
import com.example.caminoalba.models.dto.Comment;
import com.example.caminoalba.ui.menuItems.publication.recyclers.RecyclerAdapterComments;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FragmentComment extends Fragment {


    private RecyclerView recyclerView;
    private Button btnSendComment;
    private Publication publication;
    private boolean isUserList;
    private Profile profile;
    private EditText etComment;
    private SharedPreferences sharedPreferences;
    private boolean isNews;

    public FragmentComment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getChildFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    // If there are fragments in the back stack, pop the topmost fragment
                    fragmentManager.popBackStack();
                } else {
                    try {
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_comment);
                        if (isNews) {
                            navController.navigate(R.id.newsFragment);
                        } else if (isUserList) {
                            // Create an instance of the child fragment
                            FragmentUserPublications fragmentUserPublications = new FragmentUserPublications();
                            // Begin a new FragmentTransaction using the getChildFragmentManager() method
                            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                            // Add the child fragment to the transaction and specify a container view ID in the parent layout
                            transaction.replace(R.id.fragment_comment, fragmentUserPublications);
                            transaction.addToBackStack(null); // Add the fragment to the back stack
                            transaction.commit();
                        } else {
                            navController.navigate(R.id.blogFragment);
                        }
                    } catch (IllegalArgumentException e) {
                        // Handle the exception here, for example, log the error or perform a fallback action
                        Log.e("FragmentComment", "Error finding NavController: " + e.getMessage());
                    }
                }
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);

        return inflater.inflate(R.layout.fragment_comment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        etComment = view.findViewById(R.id.etComment);
        btnSendComment = view.findViewById(R.id.btnComment);
        recyclerView = view.findViewById(R.id.recycler_view_comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Bundle bundle = getArguments();
        if (getArguments() != null) {
            publication = (Publication) bundle.getSerializable("publication");
            isNews = bundle.getBoolean("isNews", false);
            isUserList = bundle.getBoolean("isUserList", false);
//            profile = (Profile) bundle.getSerializable("profile");
        }
        Gson gson = new Gson();
        String profileStr = sharedPreferences.getString("profile", "");
        profile = gson.fromJson(profileStr, Profile.class);

        // Get a reference to the comments node in the database
        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference().child("comments");

        // Set up the button click listener to add a new comment to the database
        btnSendComment.setOnClickListener(v -> {
            String commentText = etComment.getText().toString();

            // Generate a unique ID for the comment and save it to the database
            String commentId = commentsRef.push().getKey();

            // Save the comment to the Firebase database
            Comment comment = new Comment();
            comment.setPublicationId(publication.getPublication_id());
            comment.setPublication(publication);
            comment.setProfile(profile);
            comment.setId(commentId);
            comment.setCommentText(commentText);
            LocalDateTime datePublished = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = datePublished.format(formatter);
            comment.setDatePublished(formattedDate);
            assert commentId != null;
            commentsRef.child(commentId).setValue(comment);

            // Notify the user that the comment was successfully posted
            Toast.makeText(getContext(), "Comment posted!", Toast.LENGTH_SHORT).show();

            // Clear the comment text field
            etComment.setText("");
        });

        // Query the comments node and filter by the publication ID to fetch the comments for this publication
        Query query = commentsRef.orderByChild("publicationId").equalTo(publication.getPublication_id());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comment> comments = new ArrayList<>();
                for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    comments.add(comment);
                }

                // Create and set the adapter for the RecyclerView
                RecyclerAdapterComments adapter = new RecyclerAdapterComments(comments, getContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });


//        // Get FragmentManager
//        FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
//
//        // Navigate back to previous fragment
//        fragmentManager.popBackStack();


    }


}