package com.serialmmf.Anbattery.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.serialmmf.Anbattery.receivers.MemoryBroadcastReceiver;

public class MemoryService extends Service {
    public MemoryService() {
    }

    @Override
    public void onCreate() {
        MemoryBroadcastReceiver mReceiver = new MemoryBroadcastReceiver();
        this.registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
