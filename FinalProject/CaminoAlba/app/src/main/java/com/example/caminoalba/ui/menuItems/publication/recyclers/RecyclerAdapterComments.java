package com.example.caminoalba.ui.menuItems.publication.recyclers;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.caminoalba.R;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.dto.Comment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapterComments extends RecyclerView.Adapter<RecyclerAdapterComments.CommentViewHolder> {

    private final List<Comment> commentList;
    private final Context context;
    private SharedPreferences sharedPreferences;
    private Profile globalProfile;

    public RecyclerAdapterComments(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String profileStr = sharedPreferences.getString("profile", "");
        Gson gson = new Gson();
        this.globalProfile = gson.fromJson(profileStr, Profile.class);
    }

    @NonNull
    @Override
    public RecyclerAdapterComments.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterComments.CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        assert comment != null;
        holder.init(comment);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivProfilePhoto;
        private final TextView tvComment, tvProfileName, tvDatePublished;
        private final ImageView imageButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePhoto = itemView.findViewById(R.id.comment_author_photo);
            tvComment = itemView.findViewById(R.id.comment_text);
            tvProfileName = itemView.findViewById(R.id.comment_author_name);
            tvDatePublished = itemView.findViewById(R.id.tvDatePublished);
            imageButton = itemView.findViewById(R.id.remove_button);
            imageButton.setVisibility(View.GONE);
        }

        public void init(Comment comment) {
            Profile profile = comment.getProfile();
            if (profile.getPhoto() != null) {
//                Picasso.get().load(profile.getPhoto()).into(ivProfilePhoto);
                Glide.with(context)
                        .load(profile.getPhoto())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.default_image) // Placeholder image while loading
                                .error(R.drawable.default_image)) // Image to display in case of error
                        .circleCrop() // Apply circular cropping
                        .into(ivProfilePhoto);
            }
            tvComment.setText(comment.getCommentText());
            tvProfileName.setText(profile.getFirstName() + " " + profile.getLastName());
            tvDatePublished.setText(comment.getDatePublished());
            // Set click listener to delete comment

            if (globalProfile.getUser().getUser_id().equalsIgnoreCase(profile.getProfile_id())) {
                imageButton.setVisibility(View.VISIBLE);
                imageButton.setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this comment?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("comments").child(comment.getId());
                        commentRef.removeValue();
                    });
                    builder.setNegativeButton("No", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                });
            }


            // Update profile information
            DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference().child("profiles").child(profile.getProfile_id());
            profileRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Profile updatedProfile = dataSnapshot.getValue(Profile.class);
                    if (updatedProfile != null) {
                        comment.setProfile(updatedProfile);
                        tvProfileName.setText(updatedProfile.getFirstName().toUpperCase() + " " + updatedProfile.getLastName().toUpperCase());
                        if (updatedProfile.getPhoto() != null) {
//                            Picasso.get().load(updatedProfile.getPhoto()).into(ivProfilePhoto);
                            Glide.with(context)
                                    .load(updatedProfile.getPhoto())
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.default_image) // Placeholder image while loading
                                            .error(R.drawable.default_image)) // Image to display in case of error
                                    .circleCrop() // Apply circular cropping
                                    .into(ivProfilePhoto);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Log.e(TAG, "Failed to update profile information", databaseError.toException());
                }
            });
        }


    }


}
