package com.example.eventease2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

/**
 * Class that takes information from MyFirebaseMessagingService to create notification and display it
 * Contains the methods the handle the notification and where to display it
 * @author Adeel Khan
 */
public class NotificationMaker {

    public static final String CHANNEL_ID = "your_channel_id";
    public static final String CHANNEL_NAME = "Your Channel Name";
    public static final String CHANNEL_DESCRIPTION = "Your Channel Description";

    /**
     * Method that creates the notification channel so that it can be received by the user
     */
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);

            // Register the channel with the system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Log.d("Notification Maker", CHANNEL_ID);
        }
    }

    /**
     * Method builds and displays the notification
     */
    public static void showNotification(Context context, String title, String message) {

        // Create an intent for the notification to open when clicked
        Intent intent = new Intent(context, RoleChooseActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                //.setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = (int) System.currentTimeMillis();
        Log.d("Notification Maker", title);
        Log.d("Notification Maker", message);
        notificationManager.notify(notificationId, builder.build());
    }
}
