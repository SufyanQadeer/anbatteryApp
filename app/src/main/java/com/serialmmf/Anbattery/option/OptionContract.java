package com.serialmmf.Anbattery.option;

import android.app.Activity;

/**
 * Created by juancarlos on 19/1/17.
 */

public interface OptionContract {

    interface View {

        void setWifiStatus(boolean enabled);

        void setBluetoothStatus(boolean enabled);

        void setRotationStatus(boolean enabled);

        void setModeStatus(int mode);

        void setBrightnessStatus(boolean enabled);

        void setTimeout(int timeout);

        void setAssistantStatus(boolean enabled);

        void setBatteryIndicatorStatus(boolean enabled);

        void setSmartChargeStatus(boolean status);

        void setAutomaticOptimizationStatus(boolean isVisible);

        void setBatteryLevelForAutomaticOptimization(int level);

        void setAutomaticOptimizationWifiStatus(boolean enabled);

        void setAutomaticOptimizationBluetoothStatus(boolean enabled);

        void setAutomaticOptimizationRotationStatus(boolean enabled);

        void setAutomaticOptimizationBrightnessStatus(boolean enabled);

        void setAutomaticOptimizationBrightnessLevel(int level);

        void showAutomaticOptimizationView();

        void hideAutomaticOptimizationView();
    }

    interface Presenter {
        void getStatus();

        void toggleWifi();

        void toggleBluetooth();

        void toggleRotation();

        void toggleMode();

        void toggleBrightness(Activity activity);

        void toggleTimeout();

        void toggleAssistant();

        void toggleBatteryIndicator();

        void setSmartCharge(boolean status);

        void setAutomaticOptimization(boolean status);

        void setAutomaticOptimizationBatteryLevel(int level);

        void toggleAutomaticOptimizationWifi();

        void toggleAutomaticOptimizationBlueTooth();

        void toggleAutomaticOptimizationRotation();

        void toggleAutomaticOptimizationBrightness();

        void updateAutomaticOptimizationBrightnessLevel(int level);
    }
}
