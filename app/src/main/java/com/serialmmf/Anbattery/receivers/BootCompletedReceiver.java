package com.serialmmf.Anbattery.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.serialmmf.Anbattery.services.BatteryService;

public class BootCompletedReceiver extends BroadcastReceiver
{
  @Override
  public void onReceive(Context context, Intent intent)
  {
    Intent service_intent = new Intent(context, BatteryService.class);
    context.startService(service_intent);
  }
}
