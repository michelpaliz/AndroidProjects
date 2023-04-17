package com.example.caminoalba.ui.menuItems.publication.recyclers;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.caminoalba.R;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.Publication;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerPublicationAdapter extends RecyclerView.Adapter<RecyclerPublicationAdapter.PublicationViewHolder> {

    private final List<Publication> publicationList;
    private OnPublicationClickListener onPublicationClickListener;
    private Profile profile;

    public RecyclerPublicationAdapter(List<Publication> publicationList, Profile profile) {
        this.publicationList = publicationList;
        this.profile = profile;
    }

    public interface OnPublicationClickListener {
        void onPublicationClick(String publicationId);
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
        holder.init(publication);
    }

    @Override
    public int getItemCount() {
        if (publicationList == null) {
            return 0;
        }
        return publicationList.size();
    }

    public class PublicationViewHolder extends RecyclerView.ViewHolder {

        private final ImageView authorPhoto;
        private final TextView tvAuthorName;
        private final TextView tvTimeDisplayed;
        private final TextView tvTitleField;
        private final TextView etDescriptionField;
        private final ImageView ivDeletePublication;
        private final RecyclerView rvPhotoGrid;
        private RecyclerAdapterPublicationPhotos recyclerAdapterPublicationPhotos;


        public PublicationViewHolder(@NonNull View itemView) {
            super(itemView);
            authorPhoto = itemView.findViewById(R.id.authorPhoto_frg_publication);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName_frg_publication);
            tvTimeDisplayed = itemView.findViewById(R.id.tvTimeDisplayed_frg_publication);
            tvTitleField = itemView.findViewById(R.id.tvTitleField_frg_publication);
            etDescriptionField = itemView.findViewById(R.id.etDescriptionField_frg_publication);
            rvPhotoGrid = itemView.findViewById(R.id.rvPhotoGrid_frg_publication);
            ivDeletePublication = itemView.findViewById(R.id.close_button);
            // create and set the adapter for the inner RecyclerView
            recyclerAdapterPublicationPhotos = new RecyclerAdapterPublicationPhotos(new ArrayList<>());
            rvPhotoGrid.setAdapter(recyclerAdapterPublicationPhotos);
            rvPhotoGrid.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }

        public void init(Publication publication) {

            // update the adapter for the inner RecyclerView with the new list of URIs

//            if (publication.getBlog().getProfile().getPhoto() != null) {
//                Picasso.get().load(publication.getBlog().getProfile().getPhoto()).into(authorPhoto);
//            }

            // Check if the photo URL is null or empty, and update it with the latest profile photo if available
            if (TextUtils.isEmpty(publication.getBlog().getProfile().getPhoto())) {
                DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("profiles").child(publication.getBlog().getProfile().getProfile_id());
                profileRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Profile updatedProfile = snapshot.getValue(Profile.class);
                        if (updatedProfile != null && !TextUtils.isEmpty(updatedProfile.getPhoto())) {
                            publication.getBlog().getProfile().setPhoto(updatedProfile.getPhoto());
                            // Load the profile photo into the ImageView using Picasso
                            Picasso.get().load(updatedProfile.getPhoto()).into(authorPhoto);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            } else {
                // Load the profile photo into the ImageView using Picasso
                Picasso.get().load(publication.getBlog().getProfile().getPhoto()).into(authorPhoto);
            }


            recyclerAdapterPublicationPhotos.setPhotos(publication.getPhotos());
            recyclerAdapterPublicationPhotos = (RecyclerAdapterPublicationPhotos) rvPhotoGrid.getAdapter();
            recyclerAdapterPublicationPhotos.notifyDataSetChanged();
            tvAuthorName.setText(publication.getBlog().getProfile().getFirstName());
            tvTimeDisplayed.setText(publication.getDatePublished());
            tvTitleField.setText(publication.getTitle());
            etDescriptionField.setText(publication.getDescription());
            // Set click listener on close button
            ivDeletePublication.setVisibility(View.GONE);
            if (publication.getBlog().getBlog_id().equalsIgnoreCase(profile.getProfile_id())){
                ivDeletePublication.setVisibility(View.VISIBLE);
                ivDeletePublication.setOnClickListener(v -> {
                    // Get the ID of the publication to be deleted
                    String publicationId = publication.getPublication_id();
                    onPublicationClickListener.onPublicationClick(publicationId);
                });
            }


        }






    }



}