package com.serialmmf.Anbattery.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.serialmmf.Anbattery.R;

/**
 * Created by juancarlos on 23/1/17.
 */

public class NotificationSender {

    public static void send(Context context, String text) {

        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.reminder_layout_file);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                // Set Icon
                .setSmallIcon(R.drawable.ic_launcher)
                // Dismiss Notification
                .setAutoCancel(true)
                // Set RemoteViews into Notification
                .setContent(remoteViews)
                .setPriority(Notification.PRIORITY_MAX)
                .setVibrate(new long[] { 1000, 1000});

        // Locate and set the Image into custom_notification.xml ImageViews
        remoteViews.setImageViewResource(R.id.reminderimagenotileft,R.drawable.ic_launcher);
        //  remoteViews.setImageViewResource(R.id.imagenotiright, notificationBattery);

        // Locate and set the Text into custom_notification.xml TextViews
        remoteViews.setTextViewText(R.id.remindertitle, context.getString(R.string.app_name));
        remoteViews.setTextViewText(R.id.remindertext, text);

        // Build Notification with Notification Manager
        notificationmanager.notify(1, builder.build());
    }
}
