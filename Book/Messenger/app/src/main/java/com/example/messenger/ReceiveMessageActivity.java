package com.example.messenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ReceiveMessageActivity extends Activity {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    //Recogemos el string que tiene como clave "message"
    //int intMessage = intent.getIntExtra("name",2);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recieve_message);
        Intent intent = getIntent();
        String messageText = intent.getStringExtra(EXTRA_MESSAGE);
        TextView tvRecieveMessage =  findViewById(R.id.tvReciveMessage);
        //Cuando el mensaje es recogido se lo pasamos a nuestro view para ver el msg
        tvRecieveMessage.setText(messageText);
    }
}
