package com.example.shop.functions;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.shop.R;
import com.example.shop.activities.HomeActivity;
import com.google.android.gms.common.internal.Constants;

import java.util.Random;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Create a notification

        // Get the system's notification manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if there are any active notifications, and if so, skip the rest of the code
        if (notificationManager.getActiveNotifications().length > 0) {
            for (StatusBarNotification statusBarNotification : notificationManager.getActiveNotifications()) {
                if (statusBarNotification.getId() == 1) { // the notification with ID 1 has already been created
                    return;
                }
            }
        }

        // Create an intent to open the app's HomeActivity
        Intent homeIntent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Create a pending intent that will launch the HomeActivity when the notification is tapped
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, homeIntent, PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification_channel_id")
                .setSmallIcon(R.drawable.logo) // Set the notification icon
                .setContentTitle("Hi,") // Set the notification title
                .setContentText("check the app, maybe new products have been uploaded") // Set the notification text
                .setContentIntent(pendingIntent) // Set the intent that will be triggered when the notification is tapped
                .setAutoCancel(true); // Set the notification to automatically dismiss when tapped

        // Show the notification
        notificationManager.notify(1, builder.build());
    }
}
