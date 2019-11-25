package com.serialmmf.Anbattery.option;

import android.app.Activity;

import com.serialmmf.Anbattery.battery.BatteryLevelNotificator;
import com.serialmmf.Anbattery.model.PowerManager;
import com.serialmmf.Anbattery.option.OptionContract.View;
import com.serialmmf.Anbattery.util.PrefsManager;

/**
 * Created by juancarlos on 19/1/17.
 */

public class OptionPresenter implements OptionContract.Presenter {

    private OptionContract.View mView;
    private PowerManager mPowerManager;
    private PrefsManager mPrefs;
    private BatteryLevelNotificator mBatteryLevelNotificator;

    private int mAutomaticOptimizationBrightnessLevel;
    private boolean mAutomaticOptimizationBrightnessStatus;
    private boolean mAutomaticOptimizationRotationStatus;
    private boolean mAutomaticOptimizationBluetoothStatus;
    private boolean mAutomaticOptimizationWifiStatus;
    private int mAutomaticOptimizationBatteryLevel;
    private boolean mAutomaticOptimizationStatus;
    private boolean mSmartChargeStatus;

    private int mTimeout;
    private boolean mIsBrightnessEnabled;
    private int mModeStatus;
    private boolean mIsRotationEnabled;
    private boolean mIsBlueToothEnabled;
    private boolean mIsWifiOn;

    public OptionPresenter(View view, PowerManager powerManager, PrefsManager prefs,
                           BatteryLevelNotificator batteryLevelNotificator) {
        mView = view;
        mPowerManager = powerManager;
        mPrefs = prefs;
        mBatteryLevelNotificator = batteryLevelNotificator;
    }

    @Override
    public void getStatus() {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mIsWifiOn = mPowerManager.isWifiOn();
                mIsBlueToothEnabled = mPowerManager.isBluetoothEnabled();
                mIsRotationEnabled = mPowerManager.isRotationEnabled();
                mModeStatus = mPowerManager.getMode();
                mIsBrightnessEnabled = mPowerManager.isBrightnessEnabled();
                mTimeout = mPowerManager.getTimeout();

                mSmartChargeStatus = mPrefs.smartChargeEnabled();
                mAutomaticOptimizationBatteryLevel = mPrefs.automaticOptimizationBatteryLevel();
                mAutomaticOptimizationStatus = mPrefs.automaticOptimizationEnabled();
                mAutomaticOptimizationWifiStatus = mPrefs.automaticOptimizationWifiEnabled();
                mAutomaticOptimizationBluetoothStatus = mPrefs.automaticOptimizationBluetoothEnabled();
                mAutomaticOptimizationRotationStatus = mPrefs.automaticOptimizationRotationEnabled();
                mAutomaticOptimizationBrightnessStatus= mPrefs.automaticOptimizationBrightnessEnabled();
                mAutomaticOptimizationBrightnessLevel = mPrefs.automaticOptimizationBrightnessLevel() - 20;

                mView.setWifiStatus(mIsWifiOn);
                mView.setBluetoothStatus(mIsBlueToothEnabled);
                mView.setRotationStatus(mIsRotationEnabled);
                mView.setModeStatus(mModeStatus);
                mView.setBrightnessStatus(mIsBrightnessEnabled);
                mView.setTimeout(mTimeout);

                mView.setAssistantStatus(mPrefs.isAssistantEnabled());
                mView.setBatteryIndicatorStatus(mPrefs.batteryIndicatorIsEnabled());

                mView.setSmartChargeStatus(mSmartChargeStatus);
                mView.setAutomaticOptimizationStatus(mAutomaticOptimizationStatus);
                mView.setBatteryLevelForAutomaticOptimization(mAutomaticOptimizationBatteryLevel);
                mView.setAutomaticOptimizationWifiStatus(mAutomaticOptimizationWifiStatus);
                mView.setAutomaticOptimizationBluetoothStatus(mAutomaticOptimizationBluetoothStatus);
                mView.setAutomaticOptimizationRotationStatus(mAutomaticOptimizationRotationStatus);
                mView.setAutomaticOptimizationBrightnessStatus(mAutomaticOptimizationBrightnessStatus);
                mView.setAutomaticOptimizationBrightnessLevel(mAutomaticOptimizationBrightnessLevel);
            }
        }).start();
    }

    @Override
    public void toggleWifi() {

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mView.setWifiStatus(!mIsWifiOn);
                mIsWifiOn = mPowerManager.toggleWifi();
                mView.setWifiStatus(mIsWifiOn);
            }
        }).start();
    }

    @Override
    public void toggleBluetooth() {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mView.setBluetoothStatus(!mIsBlueToothEnabled);
                mIsBlueToothEnabled = mPowerManager.toggleBluetooth();
                mView.setBluetoothStatus(mIsBlueToothEnabled);
            }
        }).start();
    }

    @Override
    public void toggleRotation() {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mView.setRotationStatus(!mIsRotationEnabled);
                mIsRotationEnabled = mPowerManager.toggleRotation();
                mView.setRotationStatus(mIsRotationEnabled);
            }
        }).start();
    }

    @Override
    public void toggleMode() {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mView.setModeStatus(mPowerManager.toggleMode());
            }
        }).start();
    }

    @Override
    public void toggleBrightness(final Activity activity) {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mView.setBrightnessStatus(!mIsBrightnessEnabled);
                mIsBrightnessEnabled = mPowerManager.toogleBrightness(activity);
                mView.setBrightnessStatus(mIsBrightnessEnabled);
            }
        }).start();
    }

    @Override
    public void toggleTimeout() {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mView.setTimeout(mPowerManager.toggleTimeout());
            }
        }).start();
    }

    @Override
    public void toggleAssistant()
    {
        mPrefs.toggleAssistant();
        mView.setAssistantStatus(mPrefs.isAssistantEnabled());
    }

    @Override
    public void toggleBatteryIndicator()
    {
        boolean batteryIndicatorEnabled = !mPrefs.batteryIndicatorIsEnabled();

        if (batteryIndicatorEnabled) {
            mBatteryLevelNotificator.enable();
        } else {
            mBatteryLevelNotificator.disable();
        }

        mView.setBatteryIndicatorStatus(batteryIndicatorEnabled);
    }

    @Override
    public void setSmartCharge(final boolean status) {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mPrefs.setSmartChargeStatus(status);
            }
        }).start();
    }

    @Override
    public void setAutomaticOptimization(final boolean isVisible) {
        if (isVisible)
        {
            mView.showAutomaticOptimizationView();
        } else {
            mView.hideAutomaticOptimizationView();
        }

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mPrefs.setAutomaticOptimizationStatus(isVisible);
            }
        }).start();
    }

    @Override
    public void setAutomaticOptimizationBatteryLevel(final int level) {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mPrefs.setAutomaticOptimizationBatteryLevel(level);
            }
        }).start();
    }

    @Override
    public void toggleAutomaticOptimizationWifi() {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mAutomaticOptimizationWifiStatus = !mAutomaticOptimizationWifiStatus;
                mView.setAutomaticOptimizationWifiStatus(mAutomaticOptimizationWifiStatus);
                mPrefs.setAutomaticOptimizationWifiStatus(mAutomaticOptimizationWifiStatus);
            }
        }).start();
    }

    @Override
    public void toggleAutomaticOptimizationBlueTooth() {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mAutomaticOptimizationBluetoothStatus = !mAutomaticOptimizationBluetoothStatus;
                mView.setAutomaticOptimizationBluetoothStatus(mAutomaticOptimizationBluetoothStatus);
                mPrefs.setAutomaticOptimizationBluetoothStatus(mAutomaticOptimizationBluetoothStatus);
            }
        }).start();
    }

    @Override
    public void toggleAutomaticOptimizationRotation() {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mAutomaticOptimizationRotationStatus = !mAutomaticOptimizationRotationStatus;
                mView.setAutomaticOptimizationRotationStatus(mAutomaticOptimizationRotationStatus);
                mPrefs.setAutomaticOptimizationRotationStatus(mAutomaticOptimizationRotationStatus);
            }
        }).start();
    }

    @Override
    public void toggleAutomaticOptimizationBrightness() {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mAutomaticOptimizationBrightnessStatus = !mAutomaticOptimizationBrightnessStatus;
                mView.setAutomaticOptimizationBrightnessStatus(mAutomaticOptimizationBrightnessStatus);
                mPrefs.setAutomaticOptimizationBrighnessStatus(mAutomaticOptimizationBrightnessStatus);
            }
        }).start();
    }

    @Override
    public void updateAutomaticOptimizationBrightnessLevel(final int level) {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mPrefs.setAutomaticOptimizationBrightnessLevel(level + 20);
            }
        }).start();
    }
}
