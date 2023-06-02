package com.example.caminoalba.ui.menuItems.publication.recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.R;
import com.example.caminoalba.models.Badge;

import java.util.List;

public class RecyclerAdapterPathItem extends RecyclerView.Adapter<RecyclerAdapterPathItem.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    private List<Badge> items;
    private Context context;
    private OnItemClickListener listener;

    public RecyclerAdapterPathItem(List<Badge> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.path_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position).getName();
        holder.textView.setText(item);
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String item = items.get(position).getName();
                    listener.onItemClick(item);
                }
            }
        }
    }
}
