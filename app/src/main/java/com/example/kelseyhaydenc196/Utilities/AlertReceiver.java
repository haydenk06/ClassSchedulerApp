package com.example.kelseyhaydenc196.Utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.kelseyhaydenc196.Activity.MainActivity;
import com.example.kelseyhaydenc196.R;

public class AlertReceiver extends BroadcastReceiver {
    private NotificationManager myNotificationManager;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private String content;
    private int notificationID;
    public static final String ALERT_TITLE_ID = "Alert_Title_Id";
    public static final String ALERT_NOTIFICATION_ID = "Alert_Notification_Id";

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);
        myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        content = intent.getStringExtra(ALERT_TITLE_ID);
        notificationID = intent.getIntExtra(ALERT_NOTIFICATION_ID, -1);
        deliverNotification(context);
    }

    private void deliverNotification(Context context) {
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, notificationID,
                contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(content)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        myNotificationManager.notify(notificationID, builder.build());
    }

    public void createNotificationChannel(Context context) {
        myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        ///// Checks SDK version for Oreo+ for Notification Channels /////
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            ///// creates channel with parameters /////
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID, "Alert Notice", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Notifies the student that Course/Assessment starts today");
            myNotificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
