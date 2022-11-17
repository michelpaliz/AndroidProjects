package com.example.examenciclistas.Modelo;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Competition implements Serializable {
    private final String name;
    private final Cyclist[] cyclists;
    private final Stage[] stages;

    public Competition(String name, Cyclist[] cyclists, Stage[] stages) {
        this.name = name;
        this.cyclists = cyclists;
        this.stages = stages;
    }

    public String getName() {
        return name;
    }

    public Cyclist[] getCyclists() {
        return cyclists;
    }

    public Stage[] getStages() {
        return stages;
    }


    @NonNull
    @Override
    public String toString() {
        return "Competition{" +
                "cyclists=" + Arrays.toString(cyclists) +
                ", stages=" + Arrays.toString(stages) +
                '}';
    }
}
