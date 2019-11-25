package com.serialmmf.Anbattery.battery;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;

import androidx.core.app.NotificationCompat;

import com.serialmmf.Anbattery.MainActivity;
import com.serialmmf.Anbattery.R;
import com.serialmmf.Anbattery.util.PrefsManager;

/**
 * Created by Juan Carlos Alvarez Carrillo.
 */

public class BatteryLevelNotificator
{
    private static final int BATTERY_LEVEL_NOTIFICATION = 255;

    private static BatteryLevelNotificator sInstance;

    private static int mLevel = 100;
    private final Context mContext;

    private BatteryLevelNotificator(Context context) {
        mContext = context.getApplicationContext();
    }

    public static BatteryLevelNotificator getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BatteryLevelNotificator(context);
        }

        return sInstance;
    }

    public void enable() {
        getPrefsManager().setBatteryIndicatorEnable(true);

        notify(mLevel);
    }

    private PrefsManager getPrefsManager()
    {
        return PrefsManager.getInstance(mContext);
    }

    public void disable() {
        getPrefsManager().setBatteryIndicatorEnable(false);
        getNotificationManager().cancel(BATTERY_LEVEL_NOTIFICATION);
    }

    private NotificationManager getNotificationManager()
    {
        return (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void notify(int level)
    {
        mLevel = level;

        if (!getPrefsManager().batteryIndicatorIsEnabled()) return;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel mChannel = new NotificationChannel("123", "battery notification",NotificationManager.IMPORTANCE_HIGH);

            Notification batteryNotification = new NotificationCompat.Builder(mContext,"123")
                    .setTicker(level + "%")
                    .setOngoing(true)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.battery_levels, level)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                            R.drawable.ic_launcher))
                    .setContentTitle(mContext.getString(R.string.app_name))
                    .setContentText(mContext.getString(R.string.device_batery_level) + " " + level + "%")
                    .setContentIntent(PendingIntent.getActivity(mContext, 0, new Intent(mContext, MainActivity.class),
                            0))
                    .build();

            batteryNotification.flags = Notification.FLAG_ONGOING_EVENT;

            int smallIconId = mContext.getResources().getIdentifier("right_icon", "id", android.R.class.getPackage().getName());
            if (smallIconId != 0) {
                if (VERSION.SDK_INT < VERSION_CODES.N)
                {
                    batteryNotification.contentView.setViewVisibility(smallIconId, View.INVISIBLE);
                }
            }

            getNotificationManager().notify(BATTERY_LEVEL_NOTIFICATION, batteryNotification);
        }
        else {

            Notification batteryNotification = new NotificationCompat.Builder(mContext)
                    .setTicker(level + "%")
                    .setOngoing(true)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.battery_levels, level)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                            R.drawable.ic_launcher))
                    .setContentTitle(mContext.getString(R.string.app_name))
                    .setContentText(mContext.getString(R.string.device_batery_level) + " " + level + "%")
                    .setContentIntent(PendingIntent.getActivity(mContext, 0, new Intent(mContext, MainActivity.class),
                            0))
                    .build();

            batteryNotification.flags = Notification.FLAG_ONGOING_EVENT;

            int smallIconId = mContext.getResources().getIdentifier("right_icon", "id", android.R.class.getPackage().getName());
            if (smallIconId != 0) {
                if (VERSION.SDK_INT < VERSION_CODES.N)
                {
                    batteryNotification.contentView.setViewVisibility(smallIconId, View.INVISIBLE);
                }
            }

            getNotificationManager().notify(BATTERY_LEVEL_NOTIFICATION, batteryNotification);

        }


    }
}
