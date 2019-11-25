package com.serialmmf.Anbattery.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.serialmmf.Anbattery.receivers.AutomaticOptimizationReceiver;

public class BatteryService extends Service {

    AutomaticOptimizationReceiver mReceiver ;

    public BatteryService() {
    }

    @Override
    public void onCreate() {
        mReceiver = new AutomaticOptimizationReceiver();
        this.registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mReceiver);
    }
}
