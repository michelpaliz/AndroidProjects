package com.example.caminoalba.ui.menuItems.partner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.example.caminoalba.models.Badge;
import com.example.caminoalba.models.Path;
import com.example.caminoalba.models.Profile;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class RecyclerviewPartner extends RecyclerView.Adapter<RecyclerviewPartner.ViewHolder> {

    private List<Path> breakpointsInf;
    private Profile profile;
    private final Context context;
    private Set<String> earnedBadgeNames; // Store the names of the earned badges

    public RecyclerviewPartner(List<Path> breakpointsInf, Profile profile, Context context) {
        this.breakpointsInf = breakpointsInf;
        this.profile = profile;
        this.context = context;
        populateEarnedBadgeNames(); // Populate the earnedBadgeNames set
    }


    public void setBreakpointsInf(List<Path> breakpointsInf) {
        this.breakpointsInf = breakpointsInf;
    }


    private void populateEarnedBadgeNames() {
        earnedBadgeNames = new HashSet<>();
        if (profile.getBadges() != null) {
            for (Badge badge : profile.getBadges()) {
                earnedBadgeNames.add(badge.getName());
            }
        }
    }


    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
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
        holder.ivSecondPhoto.setTag(path.getName());

//        String nombre = "c" + breakpointsInf.get(position).getId();
        String nombre = breakpointsInf.get(position).getName().toLowerCase(Locale.ROOT);
        int id = context.getResources().getIdentifier(nombre, "drawable", context.getPackageName());
        holder.ivFirstPhoto.setImageResource(id);

        if (earnedBadgeNames.contains(path.getName())) {
            holder.ivSecondPhoto.setImageResource(R.drawable.badge);
            Badge earnedBadge = getEarnedBadge(path.getName());
            if (earnedBadge != null) {
                holder.tvTime.setText(earnedBadge.getDate());
            } else {
                holder.tvTime.setText("");
            }
        } else {
            holder.ivSecondPhoto.setImageResource(R.drawable.emptybadge);
            holder.tvTime.setText("");
        }
    }


    private Badge getEarnedBadge(String badgeName) {
        if (profile.getBadges() != null) {
            for (Badge badge : profile.getBadges()) {
                if (badge.getName().equalsIgnoreCase(badgeName)) {
                    return badge;
                }
            }
        }
        return null;
    }



    @Override
    public int getItemCount() {
        return breakpointsInf.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView ivFirstPhoto;
        private final ImageView ivSecondPhoto;
        private final TextView tvName, tvTime;
        private final TextView tvInformation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFirstPhoto = itemView.findViewById(R.id.profilePhoto);
            ivSecondPhoto = itemView.findViewById(R.id.another_photo);
            tvName = itemView.findViewById(R.id.tvUserId);
            tvTime = itemView.findViewById(R.id.tvTime);
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
