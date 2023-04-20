package com.example.caminoalba.ui.menuItems.publication.recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.dto.Comment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapterComments extends RecyclerView.Adapter<RecyclerAdapterComments.CommentViewHolder> {

    private List<Comment> commentList;
    private Context context;

    public RecyclerAdapterComments(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
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
        private final TextView tvComment;
        private final TextView tvProfileName;
        private final ImageView imageButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePhoto = itemView.findViewById(R.id.comment_author_photo);
            tvComment = itemView.findViewById(R.id.comment_text);
            tvProfileName = itemView.findViewById(R.id.comment_author_name);
            imageButton = itemView.findViewById(R.id.remove_button);
        }

        public void init(Comment comment) {
            Profile profile = comment.getProfile();
            if (profile.getPhoto() != null) {
                Picasso.get().load(profile.getPhoto()).into(ivProfilePhoto);
            }
            tvComment.setText(comment.getCommentText());
            tvProfileName.setText(profile.getFirstName() + " " + profile.getLastName());

            // Set click listener to delete comment
            imageButton.setOnClickListener(v -> {
                DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("comments").child(comment.getId());
                commentRef.removeValue();
            });

            // Update profile information
            DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference().child("profiles").child(profile.getProfile_id());
            profileRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Profile updatedProfile = dataSnapshot.getValue(Profile.class);
                    if (updatedProfile != null) {
                        comment.setProfile(updatedProfile);
                        tvProfileName.setText(updatedProfile.getFirstName() + " " + updatedProfile.getLastName());
                        if (updatedProfile.getPhoto() != null) {
                            Picasso.get().load(updatedProfile.getPhoto()).into(ivProfilePhoto);
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
