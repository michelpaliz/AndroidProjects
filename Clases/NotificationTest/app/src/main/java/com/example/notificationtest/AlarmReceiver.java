package com.example.notificationtest;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    MediaPlayer mp;

    @Override
    public void onReceive (Context context , Intent intent) {

        mp = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
//        mp.setLooping(true);
        mp.start();




//        Intent nextActivity = new Intent(context, ActivityNotifications.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, nextActivity,PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "androidknowledge")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle("Good Morning!")
//                .setContentText("It's time to wake up")
//                .setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(pendingIntent);
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
//        notificationManagerCompat.notify(123, builder.build());
    }
}