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
import com.example.caminoalba.models.dto.Comment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapterComments extends RecyclerView.Adapter<RecyclerAdapterComments.CommentViewHolder> {

    private final List<Comment> commentList;
    private final Context context;

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

    public void updateCommentProfiles(List<Comment> updatedComments) {
        // Update the comment profile information in the adapter
        for (int i = 0; i < commentList.size(); i++) {
            Comment comment = commentList.get(i);
            for (int j = 0; j < updatedComments.size(); j++) {
                Comment updatedComment = updatedComments.get(j);
                if (comment.getId().equals(updatedComment.getId())) {
                    comment.setProfile(updatedComment.getProfile());
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setComments(List<Comment> comments) {
        // Set the updated comments list in the adapter for the RecyclerView
        commentList.clear();
        commentList.addAll(comments);
        notifyDataSetChanged();
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
            if (comment.getProfile().getPhoto() != null) {
                Picasso.get().load(comment.getProfile().getPhoto()).into(ivProfilePhoto);
            }
            tvComment.setText(comment.getCommentText());
            tvProfileName.setText(comment.getProfile().getFirstName() + " " + comment.getProfile().getLastName());

            // Set click listener to delete comment
            imageButton.setOnClickListener(v -> {
                DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("comments").child(comment.getId());
                commentRef.removeValue();
            });
        }
    }

}

