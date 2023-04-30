package com.example.caminoalba.ui.menuItems.Partner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.example.caminoalba.models.Path;
import com.example.caminoalba.models.Profile;

import java.util.List;


public class RecyclerviewPartner extends RecyclerView.Adapter<RecyclerviewPartner.ViewHolder> {

    private final List<Path> breakpointsInf;
    private final Profile profile;

    public RecyclerviewPartner(List<Path> breakpointsInf, Profile profile) {
        this.breakpointsInf = breakpointsInf;
        this.profile = profile;
    }

    @NonNull
    @Override
    public RecyclerviewPartner.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_partner_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerviewPartner.ViewHolder holder, int position) {
        Path path = breakpointsInf.get(position);
        holder.tvName.setText(path.getName());
        holder.tvInformation.setText(path.getInformation());

        if (profile.getPathList() != null && !profile.getCurrentPath().isEmpty()) {
            //We show the badge for the specific path.
            for (int i = 0; i < profile.getPathList().size(); i++) {
                if (profile.getPathList().get(i).equalsIgnoreCase(path.getName())) {
                    holder.ivSecondPhoto.setImageResource(R.drawable.badge);
                }
            }
        } else {
            holder.ivFirstPhoto.setImageResource(R.drawable.default_image);
        }

    }

    @Override
    public int getItemCount() {
        return breakpointsInf.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivFirstPhoto;
        private final ImageView ivSecondPhoto;
        private final TextView tvName;
        private final TextView tvInformation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFirstPhoto = itemView.findViewById(R.id.profilePhoto);
            ivSecondPhoto = itemView.findViewById(R.id.another_photo);
            tvName = itemView.findViewById(R.id.tvUserId);
            tvInformation = itemView.findViewById(R.id.information);
        }

    }
}
