package com.example.shop.functions;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class NotificationService extends Service {

    // Constants for notification channel ID and name, and notification interval
    private static final String CHANNEL_ID = "notification_channel_id";
    private static final String CHANNEL_NAME = "notification_channel_name";
    private static final long NOTIFICATION_INTERVAL = 60*60 * 1000;

    // The system's alarm manager to schedule notifications
    private AlarmManager alarmManager;

    @Override
    public void onCreate() {
        super.onCreate();
        // Get the system's alarm manager
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Create a notification channel for Android 8.0 and above
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        }
        // Get the notification manager
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        // Register the notification channel with the notification manager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Schedule a notification when the service starts
        scheduleNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void scheduleNotification() {
        // Create an intent to broadcast when it's time to show the notification
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        // Create a pending intent to be triggered when the notification is shown
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        // Set the time when the notification should be shown
        long triggerTime = System.currentTimeMillis() + NOTIFICATION_INTERVAL;
        // Schedule a repeating alarm to trigger the pending intent and show the notification
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, NOTIFICATION_INTERVAL, pendingIntent);
    }
}
