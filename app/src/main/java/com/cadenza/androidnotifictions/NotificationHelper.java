package com.cadenza.androidnotifictions;


import static com.cadenza.androidnotifictions.MainActivity.channel_id;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public  class NotificationHelper {

    public static void DisplayNtification(Context context ,String title , String body){

        Intent intent = new Intent(context,ProfileActivity.class);

        PendingIntent pedingIntent = PendingIntent.getActivity(
               context,
               100,
               intent,
               PendingIntent.FLAG_CANCEL_CURRENT
        );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context,channel_id)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setContentIntent(pedingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mNotificationManeger = NotificationManagerCompat.from(context);
        mNotificationManeger.notify(1,mBuilder.build());
    }
}
