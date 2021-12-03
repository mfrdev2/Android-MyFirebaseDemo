package com.frabbi.myfirebasedemo.push_notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.frabbi.myfirebasedemo.MainActivity;
import com.frabbi.myfirebasedemo.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage sms) {
        super.onMessageReceived(sms);

        notificationBuildUP(sms);

       // getFirebaseMassage(Objects.requireNonNull(sms.getNotification()).getTitle(), sms.getNotification().getBody());
      //  sendNotification(sms.getNotification());
    }

    private void notificationBuildUP(RemoteMessage sms) {
        Log.d("msg", "onMessageReceived: " + sms.getData().get("message"));
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(sms.getNotification().getTitle())
                .setContentText(sms.getNotification().getBody()).setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }


    private void getFirebaseMassage(String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "myChannel")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(101, builder.build());

    }

    private void sendNotification(RemoteMessage.Notification rNotfy) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent rintent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, rintent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"myChannel")
                .setContentTitle(rNotfy.getTitle())
                .setContentText(rNotfy.getBody())
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

}
