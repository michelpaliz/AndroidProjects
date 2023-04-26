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
import java.util.List;


public class RecyclerviewPartner extends RecyclerView.Adapter<RecyclerviewPartner.ViewHolder> {

    private final List<Path> breakpointsInf;

    public RecyclerviewPartner(List<Path> breakpointsInf) {
        this.breakpointsInf = breakpointsInf;
    }

    @NonNull
    @Override
    public RecyclerviewPartner.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_partner_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerviewPartner.ViewHolder holder, int position) {
        Path path =  breakpointsInf.get(position);
        holder.tvName.setText(path.getName());
        holder.tvInformation.setText(path.getInformation());
        holder.ivFirstPhoto.setImageResource(R.drawable.default_image);
        holder.ivSecondPhoto.setImageResource(R.drawable.default_image);
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
            ivFirstPhoto = itemView.findViewById(R.id.first_photo);
            ivSecondPhoto = itemView.findViewById(R.id.another_photo);
            tvName = itemView.findViewById(R.id.name);
            tvInformation = itemView.findViewById(R.id.information);
        }

    }
}
