package com.germangascon.retrofitsample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.germangascon.retrofitsample.activities.login.SingInActivity;
import com.germangascon.retrofitsample.activities.login.SingUpActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button signIn, singUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        singUp = findViewById(R.id.btnSing_up);
        signIn = findViewById(R.id.btnSing_in);
        buttonAction();

    }



    public void buttonAction() {
        singUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SingUpActivity.class);
            startActivity(intent);
            finish();
        });

        signIn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SingInActivity.class);
            startActivity(intent);
            finish();
        });
    }


}