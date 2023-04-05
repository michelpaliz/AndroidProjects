package com.example.caminoalba.ui.menuItems.publication.recyclers;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapterPublicationPhotos extends RecyclerView.Adapter<RecyclerAdapterPublicationPhotos.ViewHolder> {

    private List<String> photos;

    public RecyclerAdapterPublicationPhotos(List<String> photos) {
        this.photos = photos;
    }

    @NonNull
    @Override
    public RecyclerAdapterPublicationPhotos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterPublicationPhotos.ViewHolder holder, int position) {
        Picasso.get().load(photos.get(position)).into(holder.imageView);

//        int maxHeight = 500;
//        Picasso.get()
//                .load(photos.get(position))
//                .resize(0, maxHeight)
//                .onlyScaleDown()
//                .into(holder.imageView);

    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final ImageView deleteButtonView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo_image_view);
            deleteButtonView = itemView.findViewById(R.id.delete_button);
            deleteButtonView.setVisibility(View.GONE);
        }


    }
}
