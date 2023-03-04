package com.example.models.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.models.ToDoModel;
import com.example.testfirebase.MainActivity;
import com.example.testfirebase.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoHolder> {


    private final List<ToDoModel> toDoModels;
    private final MainActivity activity;
    private FirebaseFirestore firebaseFirestore;

    public ToDoAdapter(MainActivity activity,List<ToDoModel> toDoModels ) {
        this.toDoModels = toDoModels;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ToDoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ToDoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoHolder holder, int position) {
        ToDoModel toDoModel = toDoModels.get(position);
        holder.chargeData(toDoModel);
    }

    @Override
    public int getItemCount() {
        return toDoModels.size();
    }

    public class ToDoHolder extends RecyclerView.ViewHolder{

        private final TextView tvDueDate;
        private final CheckBox ckCheckBox;


        public ToDoHolder(@NonNull View itemView) {
            super(itemView);
            tvDueDate = itemView.findViewById(R.id.due_date_tv);
            ckCheckBox = itemView.findViewById(R.id.mcheckbox);
        }

        public void chargeData(ToDoModel toDoModel){
            ckCheckBox.setText(toDoModel.getTask());
            tvDueDate.setText("Due on" + toDoModel.getDue());
            ckCheckBox.setChecked(toBoolean(toDoModel.getStatus()));

            ckCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked){
                    firebaseFirestore.collection("task").document(toDoModel.getTask()).update("status",1);
                }else{
                    firebaseFirestore.collection("tasks").document(toDoModel.getTask()).update("status",0);
                }
            });
        }

        /**
         *
         * @param status send the data to your component
         * @return the status of the data
         */
        private boolean toBoolean(int status){
            return status != 0;
        }


    }
}
