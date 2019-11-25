package com.serialmmf.Anbattery.option;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.serialmmf.Anbattery.R;
import com.serialmmf.Anbattery.battery.BatteryLevelNotificator;
import com.serialmmf.Anbattery.model.PowerManager;
import com.serialmmf.Anbattery.util.PrefsManager;

import static android.media.AudioManager.RINGER_MODE_NORMAL;
import static android.media.AudioManager.RINGER_MODE_SILENT;
import static android.media.AudioManager.RINGER_MODE_VIBRATE;



public class OptionFragment extends Fragment implements OptionContract.View, OnClickListener,
        OnCheckedChangeListener, OnItemSelectedListener, OnSeekBarChangeListener {

    private OptionContract.Presenter mPresenter;

    private AdRequest mAdRequest;
    private Switch mAutomaticOptimizationSwitch;
    private RelativeLayout mAutomaticOptimizationView;
    private FloatingActionButton mWifiButton;
    private FloatingActionButton mBluetoothButton;
    private FloatingActionButton mRotationButton;
    private FloatingActionButton mModeButton;
    private FloatingActionButton mBrightnessButton;
    private FloatingActionButton mTimeoutButton;
    private Spinner mAutomaticOptimizationBatteryLevel;
    private FloatingActionButton mAutomaticOptimizationWifiButton;
    private FloatingActionButton mAutomaticOptimizationBluetoothButton;
    private FloatingActionButton mAutomaticOptimizationRotationButton;
    private FloatingActionButton mAutomaticOptimizationBrightnessButton;
    private SeekBar mAutomaticOptimizationBrightnessSeekbar;
    private Switch mSmartChargeSwitch;
    private FloatingActionButton mAssistantButton;
    private FloatingActionButton mBatteryIndicatorButton;

    private void getViews(View rootView) {
        mWifiButton = (FloatingActionButton) rootView.findViewById(R.id.wifi_floatingButton);
        mBluetoothButton = (FloatingActionButton) rootView.findViewById(R.id.bluetooth_floatingButton);
        mRotationButton = (FloatingActionButton) rootView.findViewById(R.id.rotation_floatingButton);
        mModeButton = (FloatingActionButton) rootView.findViewById(R.id.mode_floatingButton);
        mBrightnessButton = (FloatingActionButton) rootView.findViewById(R.id.bright_floating_button);
        mTimeoutButton = (FloatingActionButton) rootView.findViewById(R.id.timeout_floatingButton);

        mAssistantButton = (FloatingActionButton) rootView.findViewById(R.id.asistant_floatingButton);
        mBatteryIndicatorButton = (FloatingActionButton) rootView.findViewById(R.id.battery_indicator_floatingButton);

        mSmartChargeSwitch = (Switch) rootView.findViewById(R.id.smart_charge_switch);
        mAutomaticOptimizationSwitch = (Switch) rootView.findViewById(R.id.automatic_optimization_switch);

        mAutomaticOptimizationView = (RelativeLayout) rootView.findViewById(R.id.automatic_optimization_linearLayout);
        mAutomaticOptimizationBatteryLevel = (Spinner) rootView.findViewById(R.id.automatic_optimization_battery_level_spinner);
        mAutomaticOptimizationWifiButton = (FloatingActionButton) rootView.findViewById(R.id.automatic_wifi_floatingButton);
        mAutomaticOptimizationBluetoothButton = (FloatingActionButton) rootView.findViewById(R.id.automatic_bluetooth_floatingButton);
        mAutomaticOptimizationRotationButton = (FloatingActionButton) rootView.findViewById(R.id.automatic_rotation_floatingButton);
        mAutomaticOptimizationBrightnessButton = (FloatingActionButton) rootView.findViewById(R.id.automatic_brightness_floating_button);
        mAutomaticOptimizationBrightnessSeekbar = (SeekBar) rootView.findViewById(R.id.automatic_brightness_seekBar);

        mWifiButton.setOnClickListener(this);
        mBluetoothButton.setOnClickListener(this);
        mRotationButton.setOnClickListener(this);
        mModeButton.setOnClickListener(this);
        mBrightnessButton.setOnClickListener(this);
        mTimeoutButton.setOnClickListener(this);

        mAssistantButton.setOnClickListener(this);
        mBatteryIndicatorButton.setOnClickListener(this);

        mSmartChargeSwitch.setOnCheckedChangeListener(this);
        mAutomaticOptimizationSwitch.setOnCheckedChangeListener(this);

        mAutomaticOptimizationBatteryLevel.setOnItemSelectedListener(this);
        mAutomaticOptimizationWifiButton.setOnClickListener(this);
        mAutomaticOptimizationBluetoothButton.setOnClickListener(this);
        mAutomaticOptimizationRotationButton.setOnClickListener(this);
        mAutomaticOptimizationBrightnessButton.setOnClickListener(this);
        mAutomaticOptimizationBrightnessSeekbar.setOnSeekBarChangeListener(this);

    }

    public OptionFragment() {

    }

    public static OptionFragment newInstance() {

        Bundle args = new Bundle();

        OptionFragment fragment = new OptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdRequest = new AdRequest.Builder().build();
        mPresenter = new OptionPresenter(this, PowerManager.getInstance(getActivity()),
                PrefsManager.getInstance(getActivity()), BatteryLevelNotificator.getInstance(getActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_option, container, false);

        initAdView(rootView);

        setBackground(rootView);

        getViews(rootView);

        return rootView;
    }

    private void setBackground(View rootView)
    {

    }

    private boolean isNougatOrHigher()
    {
        return (VERSION.SDK_INT >= VERSION_CODES.N);
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.getStatus();
    }

    private void initAdView(View rootView) {
        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        mAdView.loadAd(mAdRequest);
    }

    @Override
    public void setWifiStatus(boolean enabled) {
        final int state = enabled? R.drawable.ic_wifi_on : R.drawable.ic_wifi_off;

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mWifiButton.setImageResource(state);
            }
        });
    }

    @Override
    public void setBluetoothStatus(boolean enabled) {
        final int state = enabled? R.drawable.ic_bluetooth_on : R.drawable.ic_bluetooth_off;

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mBluetoothButton.setImageResource(state);
            }
        });
    }

    @Override
    public void setRotationStatus(boolean enabled) {
        final int state = enabled? R.drawable.ic_rotation_on : R.drawable.ic_rotation_off;

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mRotationButton.setImageResource(state);
            }
        });
    }

    @Override
    public void setModeStatus(int mode) {
        final int state;
        switch (mode) {
            case RINGER_MODE_SILENT:
                state = R.drawable.ic_mode3;
                break;
            case RINGER_MODE_VIBRATE:
                state = R.drawable.ic_mode1;
                break;
            case RINGER_MODE_NORMAL:
                state = R.drawable.ic_mode2;
                break;
            default:
                state = R.drawable.ic_mode1;
        }

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mModeButton.setImageResource(state);
            }
        });
    }

    @Override
    public void setBrightnessStatus(boolean enabled) {
        final int state = enabled? R.drawable.ic_bright_on : R.drawable.ic_bright_off;

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mBrightnessButton.setImageResource(state);
            }
        });
    }

    @Override
    public void setTimeout(int timeout) {
        final int state;

        if (timeout == 10000) {
            state = R.drawable.ic_wait10;
        } else if (timeout == 30000) {
            state = R.drawable.ic_wait30;
        } else if (timeout == 45000) {
            state = R.drawable.ic_wait_45;
        } else {
            state = R.drawable.ic_wait;
        }

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mTimeoutButton.setImageResource(state);
            }
        });
    }

    @Override
    public void setAssistantStatus(boolean enabled)
    {
        final int state = enabled? R.drawable.ic_asistant_on : R.drawable.ic_asistant_off;

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mAssistantButton.setImageResource(state);
            }
        });
    }

    @Override
    public void setBatteryIndicatorStatus(boolean enabled)
    {
        final int state = enabled? R.drawable.ic_battery_indicator_on : R.drawable.ic_battery_indicator_off;

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mBatteryIndicatorButton.setImageResource(state);
            }
        });
    }

    @Override
    public void setSmartChargeStatus(final boolean status) {

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mSmartChargeSwitch.setChecked(status);
            }
        });
    }

    @Override
    public void setAutomaticOptimizationStatus(final boolean status) {

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mAutomaticOptimizationSwitch.setChecked(status);
            }
        });
    }

    @Override
    public void setBatteryLevelForAutomaticOptimization(final int level) {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mAutomaticOptimizationBatteryLevel.setSelection(level);
            }
        });
    }

    @Override
    public void setAutomaticOptimizationWifiStatus(boolean enabled) {
        final int state = enabled? R.drawable.ic_wifi_on : R.drawable.ic_wifi_off;

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mAutomaticOptimizationWifiButton.setImageResource(state);
            }
        });
    }

    @Override
    public void setAutomaticOptimizationBluetoothStatus(boolean enabled) {
        final int state = enabled? R.drawable.ic_bluetooth_on : R.drawable.ic_bluetooth_off;

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mAutomaticOptimizationBluetoothButton.setImageResource(state);
            }
        });
    }

    @Override
    public void setAutomaticOptimizationRotationStatus(boolean enabled) {
        final int state = enabled? R.drawable.ic_rotation_on : R.drawable.ic_rotation_off;

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mAutomaticOptimizationRotationButton.setImageResource(state);
            }
        });
    }

    @Override
    public void setAutomaticOptimizationBrightnessStatus(boolean enabled) {
        final int state = enabled? R.drawable.ic_bright_on : R.drawable.ic_bright_off;


        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mAutomaticOptimizationBrightnessButton.setImageResource(state);
            }
        });
    }

    @Override
    public void setAutomaticOptimizationBrightnessLevel(final int level) {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mAutomaticOptimizationBrightnessSeekbar.setProgress(level);
            }
        });
    }

    @Override
    public void showAutomaticOptimizationView() {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mAutomaticOptimizationView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideAutomaticOptimizationView() {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mAutomaticOptimizationView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wifi_floatingButton:
                mPresenter.toggleWifi();
                break;
            case R.id.bluetooth_floatingButton:
                mPresenter.toggleBluetooth();
                break;
            case R.id.rotation_floatingButton:
                mPresenter.toggleRotation();
                break;
            case R.id.mode_floatingButton:
                mPresenter.toggleMode();
                break;
            case R.id.bright_floating_button:
                mPresenter.toggleBrightness(getActivity());
                break;
            case R.id.timeout_floatingButton:
                mPresenter.toggleTimeout();
                break;
            case R.id.asistant_floatingButton:
                mPresenter.toggleAssistant();
                break;
            case R.id.battery_indicator_floatingButton:
                mPresenter.toggleBatteryIndicator();
                break;
            case R.id.automatic_wifi_floatingButton:
                mPresenter.toggleAutomaticOptimizationWifi();
                break;
            case R.id.automatic_bluetooth_floatingButton:
                mPresenter.toggleAutomaticOptimizationBlueTooth();
                break;
            case R.id.automatic_rotation_floatingButton:
                mPresenter.toggleAutomaticOptimizationRotation();
                break;
            case R.id.automatic_brightness_floating_button:
                mPresenter.toggleAutomaticOptimizationBrightness();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.smart_charge_switch) {
            mPresenter.setSmartCharge(isChecked);
        } else if (buttonView.getId() == R.id.automatic_optimization_switch) {
            mPresenter.setAutomaticOptimization(isChecked);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mPresenter.setAutomaticOptimizationBatteryLevel(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mPresenter.updateAutomaticOptimizationBrightnessLevel(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
