package com.serialmmf.Anbattery.model;

import java.util.concurrent.TimeUnit;

import static android.media.AudioManager.RINGER_MODE_SILENT;

/**
 * Created by juancarlos on 12/1/17.
 */

public class Battery {

    private static final String TAG = Battery.class.getSimpleName();

    private int mLevel;
    private int mTemperature;
    private float mVoltage;
    private String mTechnology;
    private String mHealth;

    public Battery(String health, int level, int temperature, float voltage, String technology) {
        mHealth = health;
        mLevel = Integer.valueOf(level);
        mTemperature = temperature;
        mVoltage = voltage;
        mTechnology = technology;
    }

    public int getLevel() {
        return mLevel;
    }

    public String getTemperature() {
        return mTemperature / 10 + Character.toString((char) 176) + " C";
    }

    public String getVoltage() {
        String voltage = String.format("%.2f", mVoltage / (float) 1000);

        if (!voltage.isEmpty()) {
            voltage += Character.toString((char) 176) + " V";
        }

        return voltage;
    }

    public String getTechnology() {
        return mTechnology;
    }

    public String getHealth() {
        return mHealth;
    }

    public long getRemeiningTimeInMillis(PowerManager powerManager) {
        float optimizationValue = getOptimizationValue(powerManager);

        return (((4 * Math.round((mVoltage + optimizationValue) / (float) 1000)) * 60 * 60 * 1000) / 100) * mLevel;
    }

    public String[] getRemeiningTime(PowerManager powerManager) {
        long remainingTime = getRemeiningTimeInMillis(powerManager);

        return new String[] {
                String.valueOf(TimeUnit.MILLISECONDS.toHours(remainingTime)),
                String.valueOf(TimeUnit.MILLISECONDS.toMinutes(remainingTime) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(remainingTime)))
        };
    }

    private float getOptimizationValue(PowerManager powerManager) {
        float optimizationValue = 0;
        if (!powerManager.isWifiOn()) {
            optimizationValue += 0.33;
        }
        if (!powerManager.isBluetoothEnabled()) {
            optimizationValue += 0.33;
        }
        if (!powerManager.isRotationEnabled()) {
            optimizationValue += 0.33;
        }

        if (powerManager.getMode() == RINGER_MODE_SILENT) {
            optimizationValue += 0.33;
        }

        if (!powerManager.isBrightnessEnabled()) {
            optimizationValue += 0.33;
        }

        return optimizationValue;
    }
}
