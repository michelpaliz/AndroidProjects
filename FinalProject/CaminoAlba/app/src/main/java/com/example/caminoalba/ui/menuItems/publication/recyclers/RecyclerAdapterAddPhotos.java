package com.example.caminoalba.ui.menuItems.publication.recyclers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapterAddPhotos extends RecyclerView.Adapter<RecyclerAdapterAddPhotos.ViewHolder> {

    private final List<Uri> uriList;
    private final Drawable defaultImage;

    public RecyclerAdapterAddPhotos(List<Uri> uriList, Context context) {
        this.uriList = uriList;
        this.defaultImage = context.getResources().getDrawable(R.drawable.default_image);
    }


    @NonNull
    @Override
    public RecyclerAdapterAddPhotos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterAddPhotos.ViewHolder holder, int position) {
//        holder.imageView.setImageURI(uriList.get(position));
        if (uriList.size() == 0) {
            holder.imageView.setImageDrawable(defaultImage);
        } else {
//            holder.imageView.setImageURI(uriList.get(position));
            Picasso.get().load(uriList.get(position))
                    .placeholder(R.drawable.default_image) // optional placeholder image
                    .error(R.drawable.default_image) // optional error image
                    .fit()
                    .centerCrop() // optional image cropping
                    .into(holder.imageView);

        }


    }


    @Override
    public int getItemCount() {
        int cuantity = 0;

        if (uriList != null) {
            if (uriList.size() == 0) {
                return 1;
            } else {
                cuantity = uriList.size();
            }
        }
        return cuantity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo_image_view);
            deleteButton = itemView.findViewById(R.id.delete_button);
            deletePhoto();
        }

        public void deletePhoto() {
            deleteButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (uriList.size() > 1 && position != RecyclerView.NO_POSITION) {
                    uriList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, uriList.size());
                } else {
                    // Show a message indicating that at least one photo is required
                    Toast.makeText(view.getContext(), "At least one photo is required", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
