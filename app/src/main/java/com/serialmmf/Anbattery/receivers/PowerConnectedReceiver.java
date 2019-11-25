package com.serialmmf.Anbattery.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.serialmmf.Anbattery.MainActivity;
import com.serialmmf.Anbattery.model.PowerManager;
import com.serialmmf.Anbattery.util.PrefsManager;

/**
 * Created by juancarlos on 19/1/17.
 */

public class PowerConnectedReceiver extends BroadcastReceiver {

    public static final String ACTION_SMART_CHARGE = PowerManager.class.getCanonicalName() + "smartCharge";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (PrefsManager.getInstance(context).smartChargeEnabled()) {
            Intent activity = new Intent(context, MainActivity.class);
            activity.setAction(ACTION_SMART_CHARGE);
            activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activity);


        }
    }
}
