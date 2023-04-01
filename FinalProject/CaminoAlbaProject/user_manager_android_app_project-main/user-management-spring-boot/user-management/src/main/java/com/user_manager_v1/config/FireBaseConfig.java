package com.user_manager_v1.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FireBaseConfig {

    public void initConfig() {
        FileInputStream serviceAccount;
        try {
            serviceAccount =
                    new FileInputStream("/home/michael/Downloads/caminoalbabackend-firebase-adminsdk-w53sa-c0f584ae30.json");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        FirebaseOptions options;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://caminoalbabackend-default-rtdb.europe-west1.firebasedatabase.app/")
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FirebaseApp.initializeApp(options);
        FirebaseApp app = FirebaseApp.getInstance();
        if (app != null) {
            System.out.println("FirebaseApp initialization successful.");
        } else {
            System.out.println("FirebaseApp initialization failed.");
        }
    }

}
