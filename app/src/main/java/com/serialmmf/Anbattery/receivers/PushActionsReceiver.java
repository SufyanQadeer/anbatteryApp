package com.serialmmf.Anbattery.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.serialmmf.Anbattery.MainActivity;
import com.serialmmf.Anbattery.services.ReminderService;
import com.serialmmf.Anbattery.util.PrefsManager;

/**
 * Created by juancarlos on 21/3/16.
 */
public class PushActionsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ReminderService.NOTIFICATION_CLICKED_ACTION)) {
            Intent intentStart = new Intent(context, MainActivity.class);
            intentStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentStart);

            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
                    .cancel(ReminderService.NOTIF_ID);
        }

        PrefsManager.getInstance(context).setLastNotificationTime();
    }
}
