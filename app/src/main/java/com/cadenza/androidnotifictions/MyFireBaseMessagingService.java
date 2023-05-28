package com.cadenza.androidnotifictions;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    //when new message is received this method will called
    // RemoteMessage object get message content
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //there are many notification inside the remote message
        //from this notification get notification title and content
        if(remoteMessage.getNotification() != null){
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            //now we have title of the message and body of message
            // now we display this using notification

            NotificationHelper.DisplayNtification(getApplicationContext(),title,body);


        }
    }
}
