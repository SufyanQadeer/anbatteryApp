package com.serialmmf.Anbattery.battery;


import android.app.Activity;
import android.util.Log;

import com.serialmmf.Anbattery.model.Battery;
import com.serialmmf.Anbattery.model.MemoryDataManager;
import com.serialmmf.Anbattery.model.MemoryObserver;
import com.serialmmf.Anbattery.model.PowerManager;
import com.serialmmf.Anbattery.model.Task;
import com.serialmmf.Anbattery.receivers.BatteryInfoReceiver;
import com.serialmmf.Anbattery.receivers.BatteryInfoReceiver.BatteryInfoListener;
import com.serialmmf.Anbattery.util.MemoryStringFormater;
import com.serialmmf.Anbattery.util.PrefsManager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static android.media.AudioManager.RINGER_MODE_SILENT;

/**
 * Created by juancarlos on 11/1/17.
 */

public class BatteryPresenter implements BatteryContract.Presenter, BatteryInfoListener, MemoryObserver {

    private static final String TAG = BatteryPresenter.class.getSimpleName();
    private final Activity mActivity;

    private BatteryContract.View mView;
    private PowerManager mPowerManager;
    private PrefsManager mPrefsManager;
    private int mCurrentBatteryLevel = 0;
    private int mBatteryLevel = -1;
    private boolean mBatteryFilled;
    private boolean mShowGainedTime;
    private long mCurrentRemainingTime;

    public BatteryPresenter (BatteryContract.View view, BatteryInfoReceiver batteryInfoReceiver,
                             PowerManager powerManager, PrefsManager prefsManager, Activity activity) {
        mView = view;
        mActivity = activity;
        mPowerManager = powerManager;
        mPrefsManager = prefsManager;
        mBatteryFilled = false;
        mShowGainedTime = false;
        batteryInfoReceiver.addListener(this);
    }

    @Override
    public void getBatteryData(){
    }

    @Override
    public void getIssues() {
        int issues = 0;

        if (mPowerManager.isMobileDataEnabled()) {
            issues++;
        }

        if (!mPowerManager.isAirplaneModeOn()) {
            issues++;
        }

        if (mPowerManager.isGpsEnabled()) {
            issues++;
        }

        mView.displayIssues(issues);

        if (mPowerManager.isWifiOn()
                || mPowerManager.isBluetoothEnabled()
                || mPowerManager.isRotationEnabled()
                || mPowerManager.getMode() != RINGER_MODE_SILENT
                || mPowerManager.isBrightnessEnabled()) {
            mView.showOptimizationButton();
            mShowGainedTime = false;
        } else {
            mView.hideOptimizationButton();
            mShowGainedTime = true;
        }
    }

    @Override
    public void setPowerSavingMode(Activity activity) {
        mPowerManager.startPowerSavingMode(activity);

        cleanMemory();

        if (!mShowGainedTime) {
            mCurrentRemainingTime = (long) (mCurrentRemainingTime + (mCurrentRemainingTime / 100 * 8));
        }

        String gainedTime = calculateGainedTime();

        mView.displayRemainingTime(getHoursAndMinutes(mCurrentRemainingTime));

        if (!gainedTime.isEmpty()) {
            mView.displayGainedTime(calculateGainedTime());
        }

    }

    private void fillBattery() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mBatteryLevel == mCurrentBatteryLevel) {
                    timer.cancel();
                    mBatteryFilled = true;

                    if (mShowGainedTime) {
                        String gainedTime = calculateGainedTime();

                        if (!gainedTime.isEmpty()) {
                            mView.displayGainedTime(calculateGainedTime());
                        }
                    }

                    Log.i(TAG, "Battery timer canceled");
                } else {
                    mBatteryLevel++;
                    mView.setBatteryLevel(mBatteryLevel);
                }
            }
        }, 2000, 20);
    }

    @Override
    public void onInfoReceived(Battery battery) {
        mCurrentBatteryLevel = battery.getLevel();
        mCurrentRemainingTime = battery.getRemeiningTimeInMillis(mPowerManager);
        mPrefsManager.setLastRemainingTime(mCurrentRemainingTime);
        if (mShowGainedTime) {
            mCurrentRemainingTime = (long) (mCurrentRemainingTime + (mCurrentRemainingTime / 100 * 8));
        }

        if (mBatteryFilled) {
            mView.setBatteryLevel(mCurrentBatteryLevel);

        } else {
            fillBattery();
            mView.getBatteryLevel(mCurrentBatteryLevel);
        }

        mView.displayRemainingTime(getHoursAndMinutes(mCurrentRemainingTime));

        mView.displayTemperature(battery.getTemperature());
        mView.displayVoltage(battery.getVoltage());
        mView.displayHealth(battery.getHealth());
    }

    public void cleanMemory() {
//        mView.startCircleProcessingAnimation();
        MemoryDataManager.getInstance().cleanMemory(mActivity, null);
    }

    @Override
    public void onMemoryDataUpdated(Integer... values) {
        Log.i(TAG, "Progress: " + values[1]);
        Log.i(TAG, "Progress: " + MemoryStringFormater.formatMemSize(Long.valueOf(values[0]), 0));
    }

    @Override
    public void onMemoryScanFinished(ArrayList<Task> tasksList, long storageSize) {
        Log.i(TAG, "Liberable Memory: " + storageSize);
    }

    private String calculateGainedTime() {
        long lastRemainingTime = mPrefsManager.getLastRemainingTime();
        long lastRemainingTimeCheck = mPrefsManager.getLastRemainingTimeCheck();
        long currentTime = System.currentTimeMillis();

        mPrefsManager.setLastRemainingTime((long) (mCurrentRemainingTime - (mCurrentRemainingTime / 100 * 8)));
        mPrefsManager.setLastRemainingTimeCheck(currentTime);

        if (lastRemainingTime == 0) {
            return "";
        }

        long elapsedTime = currentTime - lastRemainingTimeCheck;
        long remainingTimeDif = mCurrentRemainingTime - lastRemainingTime;

        if (remainingTimeDif > 60000 && remainingTimeDif > elapsedTime) {
            String[] gainedTime = getHoursAndMinutes(remainingTimeDif);
            return gainedTime[0] + "h " + gainedTime[1] + "m";
        }

        return "";
    }

    private String[] getHoursAndMinutes(long time) {
        return new String[] {
                String.valueOf(TimeUnit.MILLISECONDS.toHours(time)),
                String.valueOf(TimeUnit.MILLISECONDS.toMinutes(time) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)))
        };
    }
}
