package com.example.caminoalba.ui.menuItems.publication.recyclers;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.caminoalba.R;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.Publication;
import java.util.ArrayList;
import java.util.List;

public class RecyclerPublicationAdapter extends RecyclerView.Adapter<RecyclerPublicationAdapter.PublicationViewHolder> {

    private final List<Publication> publicationList;
    private final Context context;

    public RecyclerPublicationAdapter(List<Publication> publicationList, Context context) {
        this.publicationList = publicationList;
        this.context = context;
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
        // set data to views
        Profile profile = publication.getBlog().getProfile();
        if (profile.getPhoto() != null){
            Uri authorPhoto = Uri.parse(publication.getBlog().getProfile().getPhoto());
            holder.authorPhoto.setImageURI(authorPhoto);
        }
        holder.tvAuthorName.setText(publication.getBlog().getProfile().getFirstName());
        holder.tvTimeDisplayed.setText(publication.getDatePublished());
        holder.tvTitleField.setText(publication.getTitle());
        holder.etDescriptionField.setText(publication.getDescription());
        // set photo adapter and layout manager
        // convert to list of URIs
        List<Uri> uriList = new ArrayList<>();
        for (String uriString : publication.getPhotos()) {
            Uri uri = Uri.parse(uriString);
            uriList.add(uri);
        }
        RecyclerAdapterPhotos recyclerAdapterPhotos = new RecyclerAdapterPhotos(uriList,context);
        holder.rvPhotoGrid.setAdapter(recyclerAdapterPhotos);
        holder.rvPhotoGrid.setLayoutManager(new GridLayoutManager(context, 4));
        holder.rvPhotoGrid.setAdapter(recyclerAdapterPhotos);
        holder.rvPhotoGrid.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public int getItemCount() {
        if (publicationList== null){
            return 0;
        }
        return publicationList.size();
    }

    public static class PublicationViewHolder extends RecyclerView.ViewHolder {

        private final ImageView authorPhoto;
        private final TextView tvAuthorName;
        private final TextView tvTimeDisplayed;
        private final TextView tvTitleField;
        private final TextView etDescriptionField;
        private final RecyclerView rvPhotoGrid;

        public PublicationViewHolder(@NonNull View itemView) {
            super(itemView);
            authorPhoto = itemView.findViewById(R.id.authorPhoto_frg_publication);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName_frg_publication);
            tvTimeDisplayed = itemView.findViewById(R.id.tvTimeDisplayed_frg_publication);
            tvTitleField = itemView.findViewById(R.id.tvTitleField_frg_publication);
            etDescriptionField = itemView.findViewById(R.id.etDescriptionField_frg_publication);
            rvPhotoGrid = itemView.findViewById(R.id.rvPhotoGrid_frg_publication);
        }
    }
}