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
        if (publication.getPhotos() == null){
            throw new NullPointerException();
        }

        // set photo adapter and layout manager
        // convert to list of URIs
//        List<Uri> uriList = new ArrayList<>();
//        for (String uriString : publication.getPhotos()) {
//            Uri uri = Uri.parse(uriString);
//            uriList.add(uri);
//        }
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
        private final RecyclerView rvPhotoGrid;

        public PublicationViewHolder(@NonNull View itemView) {
            super(itemView);
            authorPhoto = itemView.findViewById(R.id.authorPhoto_frg_publication);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName_frg_publication);
            tvTimeDisplayed = itemView.findViewById(R.id.tvTimeDisplayed_frg_publication);
            tvTitleField = itemView.findViewById(R.id.tvTitleField_frg_publication);
            etDescriptionField = itemView.findViewById(R.id.etDescriptionField_frg_publication);
            rvPhotoGrid = itemView.findViewById(R.id.rvPhotoGrid_frg_publication);
            // create and set the adapter for the inner RecyclerView
            RecyclerAdapterAddPhotos recyclerAdapterAddPhotos = new RecyclerAdapterAddPhotos(null, context, new ArrayList<>());
            rvPhotoGrid.setAdapter(recyclerAdapterAddPhotos);
            rvPhotoGrid.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }

        public void init(Publication publication){

            // update the adapter for the inner RecyclerView with the new list of URIs
            RecyclerAdapterAddPhotos recyclerAdapterAddPhotos = (RecyclerAdapterAddPhotos) rvPhotoGrid.getAdapter();
            assert recyclerAdapterAddPhotos != null;
            recyclerAdapterAddPhotos.setPhotos(publication.getPhotos());

            if (publication.getBlog().getProfile().getPhoto() != null) {
                Uri photo = Uri.parse(publication.getBlog().getProfile().getPhoto());
                authorPhoto.setImageURI(photo);
            }
            tvAuthorName.setText(publication.getBlog().getProfile().getFirstName());
            tvTimeDisplayed.setText(publication.getDatePublished());
            tvTitleField.setText(publication.getTitle());
            etDescriptionField.setText(publication.getDescription());
        }
    }
}