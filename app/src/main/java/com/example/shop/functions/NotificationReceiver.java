package com.example.shop.functions;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.shop.R;
import com.google.android.gms.common.internal.Constants;

import java.util.Random;

public class NotificationReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Create a notification
/*        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.face1)
                .setContentTitle("Want to be in the top 10?")
                .setContentText("Come back to Speed. and check your leaderboard spot.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);*/


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager.getActiveNotifications().length > 0) {
            for (StatusBarNotification statusBarNotification : notificationManager.getActiveNotifications()) {
                if (statusBarNotification.getId() == 1) {
                    return;
                }
            }
        }


            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification_channel_id")
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Hi,")
                    .setContentText("check the app, maybe new products have been uploaded");

            notificationManager.notify(1, builder.build());


    }
}