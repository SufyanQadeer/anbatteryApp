package com.serialmmf.Anbattery.model;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.serialmmf.Anbattery.R;
import com.serialmmf.Anbattery.util.NotificationSender;
import com.serialmmf.Anbattery.util.PrefsManager;

import java.lang.reflect.Method;
import java.util.List;

import static android.media.AudioManager.RINGER_MODE_NORMAL;
import static android.media.AudioManager.RINGER_MODE_SILENT;
import static android.media.AudioManager.RINGER_MODE_VIBRATE;

/**
 * Created by juancarlos on 19/1/17.
 */

public class PowerManager {

    private static final String TAG = PowerManager.class.getSimpleName();

    private static PowerManager sPowerManager;

    private Context mAppContext;
    private int mBrightness;
    private int mRotate;
    private int mTimeout;

    private PowerManager(Context context) {
        mAppContext = context.getApplicationContext();
        init();
    }

    public static PowerManager getInstance(Context context)
    {
        if (sPowerManager == null)
        {
            sPowerManager = new PowerManager(context);
        }

        return sPowerManager;
    }

    public void startPowerSavingMode(Activity activity) {
        ContentResolver cResolver = mAppContext.getContentResolver();
        Window window = activity.getWindow();
        List<ApplicationInfo> packages;
        PackageManager pm = mAppContext.getPackageManager();
        packages = pm.getInstalledApplications(0);

        ActivityManager mAppContextManager = (ActivityManager) mAppContext.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ApplicationInfo packageInfo : packages) {
            Log.e("pakages", packages + "");
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) continue;
            mAppContextManager.killBackgroundProcesses(packageInfo.packageName);
        }

        WifiManager wifiManager = (WifiManager) mAppContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
        } else {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
            }
        }

        if (mBrightness > 80) {
            setBrightness(80, activity);
        }

        mRotate = 0;
        Settings.System.putInt(mAppContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, mRotate); //0 means off, 1 means on

//        AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
//        am.setRingerMode(RINGER_MODE_SILENT);

        setTimeout(0);
        mTimeout = 10000;
    }

    public void startSmartCharge(Activity activity) {
        startPowerSavingMode(activity);
        NotificationSender.send(mAppContext, mAppContext.getString(R.string.smart_charge_activated));
    }

    public void startAutomaticOptimization(Activity activity) {
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = mAppContext.getPackageManager();
        //get a list of installed apps.
        packages = pm.getInstalledApplications(0);
        PrefsManager prefsManager = PrefsManager.getInstance(mAppContext);

        ActivityManager mAppContextManager = (ActivityManager) mAppContext.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        //  String myPackage = getApplicationContext().getPackageName();
        for (ApplicationInfo packageInfo : packages) {
            Log.e("pakages", packages + "");
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) continue;
            //if (packageInfo.packageName.equals(myPackage)) continue;
            mAppContextManager.killBackgroundProcesses(packageInfo.packageName);
        }

        if (prefsManager.automaticOptimizationBrightnessEnabled()) {
            setBrightness(prefsManager.automaticOptimizationBrightnessLevel(), activity);
        }

        if (prefsManager.automaticOptimizationWifiEnabled()) {
            WifiManager wifiManager = (WifiManager) mAppContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(false);
        }

        if (prefsManager.automaticOptimizationBluetoothEnabled()) {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
            } else {
                if (bluetoothAdapter.isEnabled()) {

                    bluetoothAdapter.disable();
                }
            }
        }

        if(prefsManager.automaticOptimizationRotationEnabled()) {
            mRotate = 0;
            Settings.System.putInt(mAppContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, mRotate);

            AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
            am.setRingerMode(RINGER_MODE_SILENT);
        }

        setTimeout(0);
        mTimeout = 10000;

        NotificationSender.send(mAppContext, mAppContext.getString(R.string.automatic_optimization_activated));
    }

    public boolean isWifiOn() {
        WifiManager wifiManager = (WifiManager) mAppContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        return wifiManager.isWifiEnabled();
    }

    public boolean toggleWifi() {
        WifiManager wifiManager = (WifiManager) mAppContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
            return false;
        } else {
            wifiManager.setWifiEnabled(true);
            return true;
        }
    }

    public boolean isBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return false;
        } else {
            return bluetoothAdapter.isEnabled();
        }
    }

    public boolean toggleBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return false;
        }

        if (isBluetoothEnabled()) {
            bluetoothAdapter.disable();
            return false;
        } else {
            bluetoothAdapter.enable();
            return true;
        }
    }

    public boolean isRotationEnabled() {
        return mRotate == 1;
    }

    public boolean toggleRotation() {
        mRotate = mRotate == 1? 0 : 1;
        Settings.System.putInt(mAppContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, mRotate); //0 means off, 1 means on

        return isRotationEnabled();
    }

    public int getMode() {
        AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);

        return am.getRingerMode();
    }

    public int toggleMode() {
        Log.i(TAG, "Toggle moder");
        AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);

        int ringerMode = am.getRingerMode();

        switch (ringerMode) {
            case RINGER_MODE_SILENT:
                ringerMode = RINGER_MODE_VIBRATE;
                break;
            case RINGER_MODE_VIBRATE:
                ringerMode = RINGER_MODE_NORMAL;
                break;
            case RINGER_MODE_NORMAL:
                ringerMode = RINGER_MODE_SILENT;
                break;
        }

        setRingerMode(ringerMode, am);

        Log.i(TAG, "Ringer moder = " + ringerMode);

        return ringerMode;
    }

    private void setRingerMode(final int ringerMode, final AudioManager am)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                am.setRingerMode(ringerMode);
            }
        }).start();
    }

    public boolean isBrightnessEnabled() {
        return mBrightness > 80;
    }

    public boolean toogleBrightness(Activity activity) {
        setBrightness(mBrightness > 80? 80 : 254, activity);

        return isBrightnessEnabled();
    }

    private void setBrightness(int brighthness, Activity activity) {
        mBrightness = brighthness;
        final Window window = activity.getWindow();
        Settings.System.putInt(mAppContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brighthness);
        final WindowManager.LayoutParams layoutpars = window.getAttributes();
        layoutpars.screenBrightness = mBrightness;

        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                window.setAttributes(layoutpars);
            }
        });
    }

    public int toggleTimeout() {
        if (mTimeout == 10000) {
            setTimeout(1);
            mTimeout = 30000;
        } else if (mTimeout == 30000) {
            setTimeout(2);
            mTimeout = 45000;
        } else if (mTimeout == 45000) {
            setTimeout(3);
            mTimeout = -1;
        } else {
            setTimeout(0);
            mTimeout = 10000;
        }

        return mTimeout;
    }

    public int getTimeout() {
        return mTimeout;
    }

    public boolean isAirplaneModeOn() {

        return Settings.System.getInt(mAppContext.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;

    }

    public boolean isGpsEnabled() {
        LocationManager ManagerForLocation = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);

        return ManagerForLocation.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public Boolean isMobileDataEnabled() {
        Object connectivityService = mAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) connectivityService;

        try {
            Class<?> c = Class.forName(cm.getClass().getName());
            Method m = c.getDeclaredMethod("getMobileDataEnabled");
            m.setAccessible(true);
            return (Boolean) m.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendMobileDataIntent() {
        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mAppContext.startActivity(intent);

    }

    public void sendAirplaneIntent() {
        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        mAppContext.startActivity(intent);
    }

    public void sendLocationIntent() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        mAppContext.startActivity(intent);
    }

    private void init() {
        ContentResolver cResolver = mAppContext.getContentResolver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(mAppContext)) {
                try {
                    Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    mBrightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
                    mRotate = Settings.System.getInt(cResolver, Settings.System.ACCELEROMETER_ROTATION);
                    mTimeout = Settings.System.getInt(cResolver, Settings.System.SCREEN_OFF_TIMEOUT);

                    if (mTimeout > 40000) {
                        setTimeout(3);
                        mTimeout = 40000;
                    }

                } catch (Settings.SettingNotFoundException e) {
                    //Throw an error case it couldn't be retrieved
                    Log.e("Error", "Cannot access system brightness");
                    e.printStackTrace();
                }
            } else {
//                writesetting_dialog.show();
            }

        } else {
            try {
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                mBrightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
                mRotate = Settings.System.getInt(cResolver, Settings.System.ACCELEROMETER_ROTATION);
                mTimeout = Settings.System.getInt(cResolver, Settings.System.SCREEN_OFF_TIMEOUT);

                Log.e("timeout", "" + mTimeout);

                if (mTimeout > 40000) {
                    setTimeout(3);
                    mTimeout = 40000;
                }

            } catch (Settings.SettingNotFoundException e) {
                //Throw an error case it couldn't be retrieved
                Log.e("Error", "Cannot access system brightness");
                e.printStackTrace();
            }
        }
    }

    private void setTimeout(final int screenOffTimeout) {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                int time;
                switch (screenOffTimeout) {
                    case 0:
                        time = 10000;
                        break;
                    case 1:
                        time = 30000;
                        break;
                    case 2:
                        time = 45000;
                        break;
                    default:
                        time = 0;
                }
                android.provider.Settings.System.putInt(mAppContext.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, time);
            }
        }).start();
    }
}
