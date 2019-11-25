package com.serialmmf.Anbattery.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.serialmmf.Anbattery.MainActivity;
import com.serialmmf.Anbattery.R;
import com.serialmmf.Anbattery.battery.BatteryLevelNotificator;
import com.serialmmf.Anbattery.util.PrefsManager;

/**
 * Created by juancarlos on 12/1/17.
 */

public class AutomaticOptimizationReceiver extends BroadcastReceiver {

    public static final String ACTION_AUTOMATIC_OPTIMIZATION = AutomaticOptimizationReceiver.class.getCanonicalName() + ".optimization";

    @Override
    public void onReceive(Context context, Intent intent) {
        PrefsManager prefsManager = PrefsManager.getInstance(context);
        int battteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

        BatteryLevelNotificator.getInstance(context).notify(battteryLevel);

        if (prefsManager.automaticOptimizationEnabled()) {
            int batteryLimit = getBatteryLimit(context, prefsManager);

            if (battteryLevel <= batteryLimit) {
                Intent activity = new Intent(context, MainActivity.class);
                activity.setAction(ACTION_AUTOMATIC_OPTIMIZATION);
                activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activity);
            }
        }
    }

    private int getBatteryLimit(Context context, PrefsManager prefsManager) {
        int limitIndex = prefsManager.automaticOptimizationBatteryLevel();
        String limit = context.getResources().getStringArray(R.array.battery_percent)[limitIndex];

        return Integer.valueOf(limit.replace("%", ""));
    }
}
