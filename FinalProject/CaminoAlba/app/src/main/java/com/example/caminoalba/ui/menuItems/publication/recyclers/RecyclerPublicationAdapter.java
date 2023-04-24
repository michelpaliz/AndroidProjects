package com.example.caminoalba.ui.menuItems.publication.recyclers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.example.caminoalba.interfaces.OnClickListener;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.Publication;
import com.example.caminoalba.ui.menuItems.publication.FragmentActionPublication;
import com.example.caminoalba.ui.menuItems.publication.FragmentComment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecyclerPublicationAdapter extends RecyclerView.Adapter<RecyclerPublicationAdapter.PublicationViewHolder> {

    private final List<Publication> publicationList;
    private OnPublicationClickListener onPublicationClickListener;
    private final Profile profile;
    private final Context context;
    private final Boolean isNews;

    public RecyclerPublicationAdapter(List<Publication> publicationList, Profile profile, Context context, boolean isNews) {
        this.publicationList = publicationList;
        this.profile = profile;
        this.context = context;
        this.isNews = isNews;
    }

    public interface OnPublicationClickListener {
        void onPublicationClick(String publicationId, Boolean remove);
    }

    public void setOnPublicationClickListener(OnPublicationClickListener onPublicationClickListener) {
        this.onPublicationClickListener = onPublicationClickListener;
    }

    @NonNull
    @Override
    public PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_publication, parent, false);
        return new PublicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicationViewHolder holder, int position) {
        Publication publication = publicationList.get(position);
//        notifyDataSetChanged();
        holder.init(publication);
    }

    @Override
    public int getItemCount() {
        if (publicationList == null) {
            return 0;
        }
        return publicationList.size();
    }

    public class PublicationViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private final ImageView authorPhoto, ivLike, ivComment;
        private final TextView tvAuthorName;
        private final TextView tvTimeDisplayed;
        private final TextView tvLikeCount;
        private final TextView tvTitleField;
        private final TextView etDescriptionField;
        private final ImageView ivDeletePublication;
        private final RecyclerView rvPhotoGrid;
        private RecyclerAdapterPublicationPhotos recyclerAdapterPublicationPhotos;
        private long likeCount;
        private final OnClickListener onClickListener;

        @Override
        public void editPublication(Publication publication) {
            //Create varibles to pass to my child fragment
            Bundle args = new Bundle();
            Gson gson = new Gson();
            String publicationJson = gson.toJson(publication);
            args.putString("publication", publicationJson);

            if (isNews) {
                if (profile.getUser().getType().equalsIgnoreCase("admin")) {
                    args.putBoolean("edit", true);
                    args.putBoolean("isNews", true);
                    // Create an instance of the child fragment
                    FragmentActionPublication fragmentActionPublication = new FragmentActionPublication();
                    //Pass the args already created to the child fragment
                    fragmentActionPublication.setArguments(args);
                    // Begin a new FragmentTransaction using the getFragmentManager() method
                    FragmentTransaction transaction = ((FragmentActivity) itemView.getContext()).getSupportFragmentManager().beginTransaction();
                    // Add the child fragment to the transaction and specify a container view ID in the parent layout
                    transaction.add(R.id.fragment_blog, fragmentActionPublication);
                    transaction.addToBackStack(null); // Add the fragment to the back stack
                    transaction.commit();
                }
            }

            if (publication.getBlog().getProfile().getProfile_id().equalsIgnoreCase(profile.getProfile_id())) {
                args.putBoolean("edit", true);
                // Create an instance of the child fragment
                FragmentActionPublication fragmentActionPublication = new FragmentActionPublication();
                //Pass the args already created to the child fragment
                fragmentActionPublication.setArguments(args);
                // Begin a new FragmentTransaction using the getFragmentManager() method
                FragmentTransaction transaction = ((FragmentActivity) itemView.getContext()).getSupportFragmentManager().beginTransaction();
                // Add the child fragment to the transaction and specify a container view ID in the parent layout
                transaction.add(R.id.fragment_blog, fragmentActionPublication);
                transaction.addToBackStack(null); // Add the fragment to the back stack
                transaction.commit();
            }



        }


        public PublicationViewHolder(@NonNull View itemView) {
            super(itemView);
            authorPhoto = itemView.findViewById(R.id.authorPhoto_frg_publication);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName_frg_publication);
            tvTimeDisplayed = itemView.findViewById(R.id.tvTimeDisplayed_frg_publication);
            tvTitleField = itemView.findViewById(R.id.tvTitleField_frg_publication);
            etDescriptionField = itemView.findViewById(R.id.etDescriptionField_frg_publication);
            rvPhotoGrid = itemView.findViewById(R.id.rvPhotoGrid_frg_publication);
            ivDeletePublication = itemView.findViewById(R.id.close_button);
            ivLike = itemView.findViewById(R.id.ivLike);
            ivComment = itemView.findViewById(R.id.ivComment);
            tvLikeCount = itemView.findViewById(R.id.tvLikesNumber);
            this.onClickListener = this;
            ivDeletePublication.setVisibility(View.GONE);
            // create and set the adapter for the inner RecyclerView
            recyclerAdapterPublicationPhotos = new RecyclerAdapterPublicationPhotos(new ArrayList<>());
            rvPhotoGrid.setAdapter(recyclerAdapterPublicationPhotos);
            rvPhotoGrid.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));

        }

        public void init(Publication publication) {
            // Get the profile reference
            DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("profiles").child(publication.getBlog().getProfile().getProfile_id());

            // Add a listener to the profile reference to listen for changes in the profile data
            profileRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Profile updatedProfile = snapshot.getValue(Profile.class);
                    if (updatedProfile != null) {
//                        // Check if the photo URL has changed and update it if necessary
//                        if (!TextUtils.equals(publication.getBlog().getProfile().getPhoto(), updatedProfile.getPhoto())) {
//                            publication.getBlog().getProfile().setPhoto(updatedProfile.getPhoto());
//                        }
                        if (!Objects.equals(publication.getBlog().getProfile(), updatedProfile)) {
                            publication.getBlog().setProfile(updatedProfile);
                        }
                    }
                    // Update the adapter with the new data
                    tvLikeCount.setText(String.valueOf(publication.getLikeCount()));
                    tvAuthorName.setText(publication.getBlog().getProfile().getFirstName().toUpperCase());
                    tvTimeDisplayed.setText(publication.getDatePublished());
                    tvTitleField.setText(publication.getTitle());
                    etDescriptionField.setText(publication.getDescription());
                    publicationCommentAction(publication);
                    publicationLikeAction(publication);
                    Picasso.get().load(publication.getBlog().getProfile().getPhoto()).into(authorPhoto);
                    recyclerAdapterPublicationPhotos.setPhotos(publication.getPhotos());
                    recyclerAdapterPublicationPhotos = (RecyclerAdapterPublicationPhotos) rvPhotoGrid.getAdapter();
                    recyclerAdapterPublicationPhotos.notifyDataSetChanged();
                    // Call publicationActions after the adapter has been updated

                    // Set click listener on close button

                    if (profile.getUser().getType().equalsIgnoreCase("admin")) {
                        ivDeletePublication.setVisibility(View.VISIBLE);
                        ivDeletePublication.setOnClickListener(v -> {
                            // Get the ID of the publication to be deleted
                            String publicationId = publication.getPublication_id();
                            onPublicationClickListener.onPublicationClick(publicationId, true);
                        });
                    }

                    itemView.setOnClickListener(v -> {
                        if (v != null) {
                            onClickListener.editPublication(publication);
                        }

                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });


        }


        public void publicationCommentAction(Publication publication) {
            ivComment.setOnClickListener(v -> {
                // Create new instance of CommentFragment
                FragmentComment commentFragment = new FragmentComment();

                // Get FragmentManager and start transaction
                FragmentManager fragmentManager = ((AppCompatActivity) itemView.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Add the CommentFragment as a child fragment
                fragmentTransaction.add(R.id.fragment_blog, commentFragment);

                // Pass the publication object as a serializable
                Bundle bundle = new Bundle();
                bundle.putSerializable("publication", publication);
                bundle.putSerializable("profile", profile);
                commentFragment.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });
        }


        public void publicationLikeAction(Publication publication) {
            DatabaseReference publicationRef = FirebaseDatabase.getInstance().getReference("publications").child(publication.getPublication_id());
            DatabaseReference userLikesRef = FirebaseDatabase.getInstance().getReference("userLikes").child(profile.getProfile_id()).child(publication.getPublication_id());

            // Check if the publication node exists in the database
            publicationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Get the current like count from the snapshot
                        Integer currentLikeCount = snapshot.child("likeCount").getValue(Integer.class);
                        if (currentLikeCount != null) {
                            likeCount = currentLikeCount;
                        }
                    }

                    // Set click listener on like button
                    ivLike.setOnClickListener(v -> {
                        // Check if the user has already liked the publication
                        userLikesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.exists()) {
                                    // User has not liked the publication before, increment the like count
                                    likeCount++;
                                    // Update the like count in the Firebase Realtime Database
                                    publicationRef.child("likeCount").setValue(likeCount);
                                    // Add the publication to the user's liked publications
                                    userLikesRef.setValue(true);
                                    // Change the color of the vector image
                                    ivLike.setColorFilter(ContextCompat.getColor(context, R.color.my_red));
                                } else {
                                    // User has already liked the publication, set the color of the vector image to red
                                    ivLike.setColorFilter(ContextCompat.getColor(context, R.color.my_red));
                                    // Remove the publication from the user's liked publications
                                    userLikesRef.removeValue();
                                    // Decrement the like count
                                    likeCount--;
                                    // Update the like count in the Firebase Realtime Database
                                    publicationRef.child("likeCount").setValue(likeCount);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle error
                            }
                        });
                    });

//                    // Check if the user has already liked the publication
                    userLikesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // User has already liked the publication, set the color of the vector image to red
                                ivLike.setColorFilter(ContextCompat.getColor(context, R.color.my_red));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }

                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }

            });


        }

    }


}