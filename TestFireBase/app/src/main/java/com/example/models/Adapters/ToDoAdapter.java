package com.example.models.Adapters;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder> {


    private List<TodoAdapter>

    @NonNull
    @Override
    public TodoAdapter.TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.TodoHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class TodoHolder extends holder{

    }
}
