package com.serialmmf.Anbattery.receivers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.serialmmf.Anbattery.MainActivity;
import com.serialmmf.Anbattery.R;
import com.serialmmf.Anbattery.services.ReminderService;
import com.serialmmf.Anbattery.util.PrefsManager;

import java.util.Date;

public class MemoryBroadcastReceiver extends BroadcastReceiver {

    private final static String BATTERY_LEVEL = "level";
    private int notificationBattery;
    private int level;
    private boolean showNotification;
    int[] arrayResources;
    Context contxt;
    public static boolean showAlarm;
    public static boolean showButtonText = true;

    public MemoryBroadcastReceiver() {

    }
    public void CustomNotification() {

//        Toast.makeText(this,"showPercentage: "+showPercentage,Toast.LENGTH_SHORT).show();
        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) contxt.getSystemService(Context.NOTIFICATION_SERVICE);
        if (!showNotification) {
            notificationmanager.cancelAll();
            return;
        } else if (showNotification) {
            // Using RemoteViews to bind custom layouts into Notification
            RemoteViews remoteViews = new RemoteViews(contxt.getPackageName(), R.layout.custom_notification);

            // Open NotificationView Class on Notification Click
            Intent intent = new Intent(contxt, MainActivity.class);

            PendingIntent pIntent = PendingIntent.getActivity(contxt, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            int id = arrayResources[level];

            NotificationCompat.Builder builder = new NotificationCompat.Builder(contxt)
                    // Set Icon
                    .setSmallIcon(R.drawable.ic_launcher)
                            // Dismiss Notification
                    .setAutoCancel(false)
                            // Make notification permanent
                    .setOngoing(true)
                            // Set PendingIntent into Notification
                    .setContentIntent(pIntent)
                            // Set RemoteViews into Notification
                    .setContent(remoteViews);

            // Locate and set the Image into custom_notification.xml ImageViews
            remoteViews.setImageViewResource(R.id.imagenotileft, R.drawable.ic_launcher);
            remoteViews.setImageViewResource(R.id.imagenotiright, notificationBattery);

            SharedPreferences pref = contxt.getSharedPreferences("CACHE", Context.MODE_PRIVATE);

//            int batteryLevelNew = 0;
//
//            if (pref.contains("batteryLevel")) {
//                batteryLevelNew = pref.getInt("batteryLevel", 0);
//            }
//            // Locate and set the Text into custom_notification.xml TextViews
//            remoteViews.setTextViewText(R.id.title, "AnBattery");
//            remoteViews.setTextViewText(R.id.text, ""+contxt.getText(R.string.status) + batteryLevelNew + '%');

            // Build Notification with Notification Manager

            notificationmanager.notify(0, builder.build());
        }
    }


    private void setNotificationImage() {
    }

    private boolean testMemBoost() {
            long l1 = PrefsManager.getInstance(contxt).lastCleanTime();

            long l2 = System.currentTimeMillis() - l1;
            if (l2 <= 600000 && l2 <= 7200000) {
                return false;// for recent boost, so we return false
            } else {
                return true;
            }
    }

    private void startAlarm() {
        AlarmManager alarmManager = (AlarmManager) contxt.getSystemService(contxt.ALARM_SERVICE);
        long when = System.currentTimeMillis();         // notification time
        Intent intent = new Intent(contxt, ReminderService.class);
        PendingIntent pendingIntent = PendingIntent.getService(contxt, 0, intent, 0);
//        alarmManager.set(AlarmManager.RTC, when, (AlarmManager.INTERVAL_HALF_HOUR), pendingIntent);
        alarmManager.set(AlarmManager.RTC, when, pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        contxt = context;
        PrefsManager prefsManager = PrefsManager.getInstance(contxt);

        long l1 = prefsManager.lastCleanTime();
        long currentTime = System.currentTimeMillis();

        Date date = new Date(l1);

        Date date2 = new Date(currentTime);

        long timeDifference = (currentTime - l1) / 1000000;

        Date date3 = new Date(timeDifference);


        if (prefsManager.notificationsActive() && prefsManager.timeForNotification()) {
            startAlarm();
        }
    }
}
