package com.example.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class TaskId  {

    @Exclude
    public String task;

    public <T extends  TaskId> T withId(@NonNull final String id){
        this.task = id;
        return (T) this;
    }



}
