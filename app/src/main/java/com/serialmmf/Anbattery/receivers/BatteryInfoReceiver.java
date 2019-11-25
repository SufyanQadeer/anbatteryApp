package com.serialmmf.Anbattery.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.serialmmf.Anbattery.R;
import com.serialmmf.Anbattery.battery.BatteryLevelNotificator;
import com.serialmmf.Anbattery.model.Battery;

import java.util.ArrayList;

import static android.os.BatteryManager.BATTERY_HEALTH_COLD;
import static android.os.BatteryManager.BATTERY_HEALTH_DEAD;
import static android.os.BatteryManager.BATTERY_HEALTH_GOOD;
import static android.os.BatteryManager.BATTERY_HEALTH_OVERHEAT;
import static android.os.BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE;
import static android.os.BatteryManager.BATTERY_HEALTH_UNKNOWN;
import static android.os.BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE;

/**
 * Created by juancarlos on 12/1/17.
 */

public class BatteryInfoReceiver extends BroadcastReceiver {

    private static ArrayList<BatteryInfoListener> mListeners = new ArrayList<>();

    public interface BatteryInfoListener {
        void onInfoReceived(Battery battery);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Todo: Pass logic to battery class
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        float voltage = (float) intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        String technology = "";

        if (intent.getExtras() != null) {
            intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
        }

        if (technology.isEmpty()) {
            technology = "---";
        }

        int battery_helth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
        String health = context.getString(R.string.unknown);

        switch (battery_helth) {
            case BATTERY_HEALTH_UNKNOWN:
                health = context.getString(R.string.unknown);
                break;
            case BATTERY_HEALTH_GOOD:
                health = context.getString(R.string.good);
                break;
            case BATTERY_HEALTH_OVERHEAT:
                health = context.getString(R.string.overheat);
                break;
            case BATTERY_HEALTH_DEAD:
                health = context.getString(R.string.dead);
                break;
            case BATTERY_HEALTH_OVER_VOLTAGE:
                health = context.getString(R.string.over_voltage);
                break;
            case BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                health = context.getString(R.string.unspecified_failure);
                break;
            case BATTERY_HEALTH_COLD:
                health = context.getString(R.string.cold);
                break;
        }

        Battery battery = new Battery(health, level, temperature, voltage, technology);

        for (BatteryInfoListener callback : mListeners) {
            callback.onInfoReceived(battery);
        }

        BatteryLevelNotificator.getInstance(context).notify(level);
    }

    public void addListener(BatteryInfoListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(BatteryInfoListener listener) {
        mListeners.remove(listener);
    }
}
