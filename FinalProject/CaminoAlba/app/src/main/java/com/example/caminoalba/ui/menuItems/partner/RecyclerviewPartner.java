package com.example.caminoalba.ui.menuItems.partner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.example.caminoalba.models.Path;
import com.example.caminoalba.models.Profile;

import java.util.List;


public class RecyclerviewPartner extends RecyclerView.Adapter<RecyclerviewPartner.ViewHolder> {

    private final List<Path> breakpointsInf;
    private final Profile profile;
    private final Context context;


    public RecyclerviewPartner(List<Path> breakpointsInf, Profile profile, Context context) {
        this.breakpointsInf = breakpointsInf;
        this.profile = profile;
        this.context = context;
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

        String nombre = "c" + breakpointsInf.get(position).getId();
        int id = context.getResources().getIdentifier(nombre, "drawable", context.getPackageName());
        holder.ivFirstPhoto.setImageResource(id);


        holder.ivSecondPhoto.setImageResource(R.drawable.emptybadge);

        if (profile.getPathList() != null && !profile.getCurrentPath().isEmpty()) {
            //We show the badge for the specific path.
            for (int i = 0; i < profile.getPathList().size(); i++) {
                if (profile.getPathList().get(i).equalsIgnoreCase(path.getName())) {
                    holder.ivSecondPhoto.setImageResource(R.drawable.badge);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return breakpointsInf.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

            // set the OnClickListener on the itemView
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // handle click event here
            int position = getAdapterPosition();
            Path path = breakpointsInf.get(position);

            // Create new instance of MyFragment
            FragmentPathInformation fragment = FragmentPathInformation.newInstance(path, true);

            // Get FragmentManager and start transaction
            FragmentManager fragmentManager = ((AppCompatActivity) itemView.getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Replace the current fragment with the new fragment
            fragmentTransaction.replace(R.id.fragment_partner, fragment);

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }
}
