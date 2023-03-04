package com.example.testfirebase;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.models.Adapters.ToDoAdapter;
import com.example.models.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton; //mFab
    private FirebaseFirestore firebaseFirestore;
    private ToDoAdapter toDoAdapter;
    private List<ToDoModel> mList;

//    https://www.youtube.com/watch?v=BawdGtpYMGM

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycerlview);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        floatingActionButton.setOnClickListener(v -> {
            //AddNewTask
        });

        mList = new ArrayList<>();
        toDoAdapter = new ToDoAdapter(MainActivity.this, mList);
        recyclerView.setAdapter(toDoAdapter);


    }

    private void showData(){
        firebaseFirestore.collection("tasks").addSnapshotListener((value, error) -> {
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if (documentChange.getType() == DocumentChange.Type.ADDED){
                    String id =  documentChange.getDocument().getId();
                    ToDoModel toDoModel = documentChange.getDocument().toObject(ToDoModel.class).withId(id);
                    mList.add(toDoModel);
                    toDoAdapter.notifyDataSetChanged();
                }
            }
            Collections.reverse(mList);
        });
    }



}