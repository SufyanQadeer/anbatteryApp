package com.serialmmf.Anbattery.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.TimeUnit;

/**
 * Created by juancarlos on 14/2/16.
 */
public class PrefsManager {
    
    private static final String SMART_CHARGE = "smartCharge";
    private static final String AUTOMATIC_OPTIMIZATION = "automaticOptimizationEnabled";
    private static final String AUTOMATIC_OPTIMIZATION_BATTERY_LEVEL = "automaticOptimizationBatteryLevel";
    private static final String AUTOMATIC_OPTIMIZATION_WIFI_STATUS = "automaticOptimizationWifiStatus";
    private static final String AUTOMATIC_OPTIMIZATION_BLUETOOTH_STATUS = "automaticOptimizationBluetoothStatus";
    private static final String AUTOMATIC_OPTIMIZATION_ROTATION_STATUS = "automaticOptimizationRotationStatus";
    private static final String AUTOMATIC_OPTIMIZATION_BRIGHTNESS_STATUS = "automaticOptimizationBrightnessSatus";
    private static final String AUTOMATIC_OPTIMIZATION_BRIGHTNESS_LEVEL = "automaticOptimizationBrightnessLevel";
    private static final String LAST_REMAINING_TIME = "lastRemainingTime";
    private static final String LAST_REMAINING_TIME_CHECK = "lastRemainingTimeCheck";
    private static final String BATTERY_INDICATOR_ENABLED = "batteryIndicatorEnabled";
    
    private static PrefsManager sPrefsManager;
    private Context mContext;

    private PrefsManager(Context context) {
         mContext = context.getApplicationContext();
    }

    public static PrefsManager getInstance(Context context) {
        if (sPrefsManager == null) {
            sPrefsManager = new PrefsManager(context);
        }
        
        return sPrefsManager;
    }
    
    private SharedPreferences getPrefs() {
        String packageName = mContext.getApplicationContext().getPackageName();
        return mContext.getSharedPreferences(packageName, Context.MODE_PRIVATE);
    }

    public String getLang() {
        return getPrefs().getString("lang", null);
    }

    public void setLang( String lang) {
        getPrefs().edit().putString("lang", lang).commit();
    }

    public  boolean notificationsActive() {
        return getPrefs().getBoolean("notificationsActive", true);
    }

    public  void setNotificationsStatus( boolean status) {
        getPrefs().edit().putBoolean("notificationsActive", status).commit();
    }

    public  long lastCleanTime() {
        return getPrefs().getLong("lastClean", 0);
    }

    public  void setLastCleanTime() {
        setLastNotificationTime();

        getPrefs().edit().putLong("lastClean", System.currentTimeMillis()).commit();
    }

    public  boolean timeForNewClean() {
        long actualTime = System.currentTimeMillis();

        return (TimeUnit.MILLISECONDS.toHours(actualTime) -
                TimeUnit.MILLISECONDS.toHours(lastCleanTime())) > 24;
    }

    public  long lastNotificationTime() {
        return getPrefs().getLong("lastNotification", 0);
    }

    public  void setLastNotificationTime() {
        getPrefs().edit().putLong("lastNotification", System.currentTimeMillis()).commit();
    }

    public  boolean timeForNotification() {
        long actualTime = System.currentTimeMillis();

        return (TimeUnit.MILLISECONDS.toHours(actualTime) -
                TimeUnit.MILLISECONDS.toHours(lastNotificationTime())) > 24;
    }

    public  boolean showBatteryAssistant() {
        return getPrefs().getBoolean("showBatteryAssistant", true);
    }

    public  void setBatteryAssistanStatus( boolean status) {
        getPrefs().edit().putBoolean("showBatteryAssistant", status).commit();
    }

    public  boolean showTaskAssistant() {
        return getPrefs().getBoolean("showTaskAssistant", true);
    }

    public  void setTaskAssistanStatus( boolean status) {
        getPrefs().edit().putBoolean("showTaskAssistant", status).commit();
    }

    public  boolean showOptionAssistant() {
        return getPrefs().getBoolean("showOptionAssistant", true);
    }

    public  void setOptionAssistanStatus(boolean status) {
        getPrefs().edit().putBoolean("showOptionAssistant", status).commit();
    }

    public void toggleAssistant() {
        boolean status = isAssistantEnabled();

        setBatteryAssistanStatus(!status);
        setTaskAssistanStatus(!status);
        setOptionAssistanStatus(!status);
    }

    public boolean isAssistantEnabled()
    {
        return (showBatteryAssistant() || showTaskAssistant() || showOptionAssistant());
    }

    public  boolean mustAskForRating() {
        if (getPrefs().getBoolean("rated", false)) {
            return false;
        }

        int starts = getPrefs().getInt("starts", 1);

        if (starts >= 4) {
            setStartsNumber(1);
            return true;
        } else {
            setStartsNumber(starts + 1);
            return false;
        }
    }

    public  void setStartsNumber( int starts) {
        getPrefs().edit().putInt("starts", starts).commit();
    }

    public  void setRated() {
        getPrefs().edit().putBoolean("rated", true).commit();
    }

    public  boolean smartChargeEnabled() {
        return getPrefs().getBoolean(SMART_CHARGE, false);
    }

    public  void setSmartChargeStatus( boolean status) {
        getPrefs().edit().putBoolean(SMART_CHARGE, status).commit();
    }

    public  boolean automaticOptimizationEnabled() {
        return getPrefs().getBoolean(AUTOMATIC_OPTIMIZATION, false);
    }

    public  void setAutomaticOptimizationStatus( boolean status) {
        getPrefs().edit().putBoolean(AUTOMATIC_OPTIMIZATION, status).commit();
    }

    public int automaticOptimizationBatteryLevel() {
        return getPrefs().getInt(AUTOMATIC_OPTIMIZATION_BATTERY_LEVEL, 1);
    }

    public  void setAutomaticOptimizationBatteryLevel( int level) {
        getPrefs().edit().putInt(AUTOMATIC_OPTIMIZATION_BATTERY_LEVEL, level).commit();
    }

    public  boolean automaticOptimizationWifiEnabled() {
        return getPrefs().getBoolean(AUTOMATIC_OPTIMIZATION_WIFI_STATUS, true);
    }

    public  void setAutomaticOptimizationWifiStatus( boolean status) {
        getPrefs().edit().putBoolean(AUTOMATIC_OPTIMIZATION_WIFI_STATUS, status).commit();
    }

    public  boolean automaticOptimizationBluetoothEnabled() {
        return getPrefs().getBoolean(AUTOMATIC_OPTIMIZATION_BLUETOOTH_STATUS, true);
    }

    public  void setAutomaticOptimizationBluetoothStatus( boolean status) {
        getPrefs().edit().putBoolean(AUTOMATIC_OPTIMIZATION_BLUETOOTH_STATUS, status).commit();
    }

    public  boolean automaticOptimizationRotationEnabled() {
        return getPrefs().getBoolean(AUTOMATIC_OPTIMIZATION_ROTATION_STATUS, true);
    }

    public  void setAutomaticOptimizationRotationStatus( boolean status) {
        getPrefs().edit().putBoolean(AUTOMATIC_OPTIMIZATION_ROTATION_STATUS, status).commit();
    }

    public  boolean automaticOptimizationBrightnessEnabled() {
        return getPrefs().getBoolean(AUTOMATIC_OPTIMIZATION_BRIGHTNESS_STATUS, true);
    }

    public  void setAutomaticOptimizationBrighnessStatus( boolean status) {
        getPrefs().edit().putBoolean(AUTOMATIC_OPTIMIZATION_BRIGHTNESS_STATUS, status).commit();
    }

    public int automaticOptimizationBrightnessLevel() {
        return getPrefs().getInt(AUTOMATIC_OPTIMIZATION_BRIGHTNESS_LEVEL, 20);
    }

    public  void setAutomaticOptimizationBrightnessLevel( int level) {
        getPrefs().edit().putInt(AUTOMATIC_OPTIMIZATION_BRIGHTNESS_LEVEL, level).commit();
    }

    public long getLastRemainingTime() {
        return getPrefs().getLong(LAST_REMAINING_TIME, 0);
    }

    public  void setLastRemainingTime(long time) {
        getPrefs().edit().putLong(LAST_REMAINING_TIME, time).commit();
    }

    public long getLastRemainingTimeCheck() {
        return getPrefs().getLong(LAST_REMAINING_TIME_CHECK, System.currentTimeMillis());
    }

    public  void setLastRemainingTimeCheck(long time) {
        getPrefs().edit().putLong(LAST_REMAINING_TIME_CHECK, time).commit();
    }

    public boolean batteryIndicatorIsEnabled() {
        return getPrefs().getBoolean(BATTERY_INDICATOR_ENABLED, true);
    }

    public  void setBatteryIndicatorEnable(boolean enabled) {
        getPrefs().edit().putBoolean(BATTERY_INDICATOR_ENABLED, enabled).commit();
    }
}
