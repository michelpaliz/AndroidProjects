package com.example.caminoalba.ui.menuItems.publication.recyclers;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;

import java.util.List;

public class RecyclerAdapterPublicationPhotos extends RecyclerView.Adapter<RecyclerAdapterPublicationPhotos.ViewHolder> {

    private final List<String> photos;

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
        holder.imageView.setImageURI(Uri.parse(photos.get(position)));
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo_image_view);
        }


    }
}
