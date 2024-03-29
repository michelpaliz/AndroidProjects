package com.example.caminoalba.ui.menuItems.publication.recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.caminoalba.R;

import java.util.List;

public class RecyclerAdapterPublicationPhotos extends RecyclerView.Adapter<RecyclerAdapterPublicationPhotos.ViewHolder> {

    private List<String> photos;
    private Context context;

    public RecyclerAdapterPublicationPhotos(List<String> photos, Context context) {
        this.context = context;
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
//        Picasso.get().load(photos.get(position)).into(holder.imageView);

        Glide.with(context)
                .load(photos.get(position))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.default_image) // Placeholder image while loading
                        .error(R.drawable.default_image)) // Image to display in case of error
                .transition(DrawableTransitionOptions.withCrossFade()) // Add transition effect
                .into(holder.imageView);
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    @Override
    public int getItemCount() {
        if (photos == null) {
            return 0;
        }
        return photos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo_image_view);
            ImageView deleteButtonView = itemView.findViewById(R.id.delete_button);
            deleteButtonView.setVisibility(View.GONE);
        }


    }
}
