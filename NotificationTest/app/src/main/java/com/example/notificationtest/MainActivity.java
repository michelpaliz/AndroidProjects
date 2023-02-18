package com.example.notificationtest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.notificationtest.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super .onCreate(savedInstanceState) ;
        setContentView(R.layout. activity_main ) ;
    }



    public void createNotification (View view) {
        int NOTIFICATION_ID = ( int ) System. currentTimeMillis () ;
        PendingIntent pendingIntent = PendingIntent. getActivity ( this, 0 , new Intent() , 0 ) ;
        Intent buttonIntent = new Intent( this, NotificationBroadcastReceiver. class ) ;
        buttonIntent.putExtra( "notificationId" , NOTIFICATION_ID) ;
        PendingIntent btPendingIntent = PendingIntent. getBroadcast ( this, 0 , buttonIntent , 0 ) ;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE ) ;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext() , default_notification_channel_id ) ;
        mBuilder.setContentTitle( "My Notification" ) ;
        mBuilder.setContentIntent(pendingIntent) ;
        mBuilder.addAction(R.drawable. ic_launcher_foreground , "Cancel" , btPendingIntent) ;
        mBuilder.setContentText( "Notification Listener Service Example" ) ;
        mBuilder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        mBuilder.setAutoCancel( true ) ;
        mBuilder.setDeleteIntent(getDeleteIntent()) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(NOTIFICATION_ID , mBuilder.build()) ;
    }


    protected PendingIntent getDeleteIntent () {
        Intent intent = new Intent(MainActivity. this,
                NotificationBroadcastReceiver. class ) ;
        intent.setAction( "notification_cancelled" ) ;
        return PendingIntent. getBroadcast (MainActivity. this, 0 , intent , PendingIntent. FLAG_CANCEL_CURRENT ) ;
    }





}