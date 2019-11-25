package com.serialmmf.Anbattery.battery;

import android.app.Activity;

/**
 * Created by juancarlos on 7/3/16.
 */
public interface BatteryContract {

    interface View {

        void setBatteryLevel(int level);

        void displayRemainingTime(String[] time);

        void displayIssues(int issues);

        void showOptimizationButton();

        void hideOptimizationButton();

        void displayTemperature(String temperature);

        void displayVoltage(String voltage);

        void displayHealth(String healt);

        void displayGainedTime(String gainedTime);

        void getBatteryLevel(int level);
    }

    interface Presenter {

        void getBatteryData();

        void getIssues();

        void setPowerSavingMode(Activity activity);
    }
}
