package com.example.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SendActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recieve_message);
        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        TextView tvRecieveMessage =findViewById(R.id.tvReciveMessage);
        tvRecieveMessage.setText(message);
    }
}
