package com.serialmmf.Anbattery.battery;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.serialmmf.Anbattery.R;
import com.serialmmf.Anbattery.dialogs.DoneDialog;
import com.serialmmf.Anbattery.dialogs.ExitDialog;
import com.serialmmf.Anbattery.dialogs.RecomendationsDialog;
import com.serialmmf.Anbattery.dialogs.RecomendationsDialog.RecomendationsDialogListener;
import com.serialmmf.Anbattery.model.PowerManager;
import com.serialmmf.Anbattery.receivers.BatteryInfoReceiver;
import com.serialmmf.Anbattery.util.PrefsManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class BatteryFragment extends Fragment implements BatteryContract.View, RecomendationsDialogListener, ExitDialog.ExitDialogListener, DoneDialog.DoneDialogListener {

    private BatteryContract.Presenter mPresenter;
    private BatteryInfoReceiver mBatteryInfoReceiver;


    private ProgressBar mBatteryProgress;
    private TextView mBatteryLoad;
    private TextView mTemperature;
    private TextView mVoltage;
    private TextView mHealth;
    private TextView mWarning;
    private ImageView mWarningIcon;
    private Button mOptimizeButton;
    private AdRequest mAdRequest;
    private TextView mHours;
    private TextView mMinutes;
    private TextView mGained;
    private InterstitialAd mInterstitialAd;

    private ProgressBar optimization_progressBar;
    private int mProgress;
    private ImageView img1, img2, img3, img4, img5, img6, img7, img8;
    //private Button btn_exit;
    int counter = 0;
    int bat_level;

    private void getViews(View rootView) {

        mBatteryProgress = (ProgressBar) rootView.findViewById(R.id.battery_load_progressBar);
        mBatteryLoad = (TextView) rootView.findViewById(R.id.battery_load_textView);
        mTemperature = (TextView) rootView.findViewById(R.id.temperature_textView);
        mVoltage = (TextView) rootView.findViewById(R.id.voltage_textView);
        mHealth = (TextView) rootView.findViewById(R.id.health_textView);
        mWarning = (TextView) rootView.findViewById(R.id.warnings_textView);
        mWarningIcon = (ImageView) rootView.findViewById(R.id.warning_imageView);
        mOptimizeButton = (Button) rootView.findViewById(R.id.optimize_button);
        mOptimizeButton.setText(getResources().getString(R.string.loading));
        mOptimizeButton.setClickable(false);
        mHours = (TextView) rootView.findViewById(R.id.hours_textView);
        mMinutes = (TextView) rootView.findViewById(R.id.minutes_textView);
        mGained = (TextView) rootView.findViewById(R.id.gained_textView);
        optimization_progressBar = (ProgressBar) rootView.findViewById(R.id.optimization_progressBar);

        img1 = (ImageView) rootView.findViewById(R.id.img1);
        img2 = (ImageView) rootView.findViewById(R.id.img2);
        img3 = (ImageView) rootView.findViewById(R.id.img3);
        img4 = (ImageView) rootView.findViewById(R.id.img4);
        img5 = (ImageView) rootView.findViewById(R.id.img5);
        img6 = (ImageView) rootView.findViewById(R.id.img6);
        img7 = (ImageView) rootView.findViewById(R.id.img7);
        img8 = (ImageView) rootView.findViewById(R.id.img8);
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interistitial_id));
        mInterstitialAd.loadAd(adRequest);

    }

    public BatteryFragment() {

    }

    public static BatteryFragment newInstance() {

        Bundle args = new Bundle();

        BatteryFragment fragment = new BatteryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdRequest = new AdRequest.Builder().build();
        mBatteryInfoReceiver = new BatteryInfoReceiver();
        mPresenter = new BatteryPresenter(this, mBatteryInfoReceiver, PowerManager.getInstance(getActivity())
                , PrefsManager.getInstance(getActivity()), getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_battery, container, false);

        initAdView(rootView);

        setBackground(rootView);

        getActivity().registerReceiver(mBatteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        getViews(rootView);

        mPresenter.getBatteryData();
        mPresenter.getIssues();
        getAllICONS();
        return rootView;
    }

    private void setBackground(View rootView) {

    }

    public void getAllICONS() {

        PackageManager pm = getActivity().getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        int count = 1;
        for (PackageInfo packInfo : packInfos) {
            if (count > 8)
                break;


            Drawable appIcon = packInfo.applicationInfo.loadIcon(pm);
            switch (count) {
                case 1:
                    img1.setImageDrawable(appIcon);
                    break;
                case 2:
                    img2.setImageDrawable(appIcon);
                    break;
                case 3:
                    img3.setImageDrawable(appIcon);
                    break;
                case 4:
                    img4.setImageDrawable(appIcon);
                    break;
                case 5:
                    img5.setImageDrawable(appIcon);
                    break;
                case 6:
                    img6.setImageDrawable(appIcon);
                    break;
                case 7:
                    img7.setImageDrawable(appIcon);
                    break;
                case 8:
                    img8.setImageDrawable(appIcon);
                    break;
            }
            count++;
        }
    }

    private boolean isNougatOrHigher() {
        return (VERSION.SDK_INT >= VERSION_CODES.N);
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            ///      getContext().unregisterReceiver(mBatteryInfoReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBatteryLevel(final int level) {
        if (!isAdded()) {
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBatteryProgress.setProgress(level);
                mBatteryLoad.setText(level + "%");
                if (bat_level == level) {
                    mOptimizeButton.setText(getResources().getString(R.string.optimize));
                    mOptimizeButton.setClickable(true);
                }
            }
        });
    }


    @Override
    public void getBatteryLevel(int level) {
        //int delay=level/8;
        bat_level = level;
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {


                if (getActivity() != null) {


                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (counter == 8) {
                                //mOptimizeButton.setVisibility(View.VISIBLE);
                                timer.cancel();
                            }
                            switch (counter) {

                                case 0:
                                    img1.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    img2.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    img3.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    img4.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    img5.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    img6.setVisibility(View.VISIBLE);
                                    break;
                                case 6:
                                    img7.setVisibility(View.VISIBLE);
                                    break;
                                case 7:
                                    img8.setVisibility(View.VISIBLE);
                                    break;
                            }
                            counter++;

                        }
                    });


                }

            }
        }, 2000, 100);

    }

    @Override
    public void displayRemainingTime(String[] time) {
        mHours.setText(time[0]);
        mMinutes.setText(time[1]);
    }

    @Override
    public void displayIssues(int issues) {
        if (issues == 0) {
            mWarning.setVisibility(View.GONE);
            mWarningIcon.setVisibility(View.GONE);
        } else {
            mWarning.setVisibility(View.VISIBLE);
            mWarningIcon.setVisibility(View.VISIBLE);

            mWarning.setText(String.valueOf(issues));

            setWarningIconClickListener();
        }
    }

    @Override
    public void showOptimizationButton() {
        //mOptimizeButton.setVisibility(View.VISIBLE);

        setOptimizeButtonClickListener();
    }

    @Override
    public void hideOptimizationButton() {
        //mOptimizeButton.setVisibility(View.GONE);
        mOptimizeButton.setText(getResources().getString(R.string.loading));
        mOptimizeButton.setClickable(false);

    }

    @Override
    public void displayTemperature(String temperature) {
        mTemperature.setText(temperature);
    }

    @Override
    public void displayVoltage(String voltage) {
        mVoltage.setText(voltage);
    }

    @Override
    public void displayHealth(String health) {
        mHealth.setText(health);
    }

    @Override
    public void displayGainedTime(final String gainedTime) {
        if (!isAdded()) {
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Animation bounce = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
                bounce.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mGained.setText(getString(R.string.gained_time) + " " + gainedTime);
                        mGained.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                ((LinearLayout) mHours.getParent()).startAnimation(bounce);
                // mHours.startAnimation(bounce);
                // mMinutes.startAnimation(bounce);
            }
        });
    }

    public void initAdView(View rootView) {
        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        mAdView.loadAd(mAdRequest);
    }

    private void setOptimizeButtonClickListener() {
        mOptimizeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOptimizeButton.getText().toString().equalsIgnoreCase(getResources().getString(R.string.exit))) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                DialogFragment exitDialog = ExitDialog.newInstance(BatteryFragment.this);
                                exitDialog.show(getFragmentManager(), "exitDialog");
                            }
                        });
                        mInterstitialAd.show();
                    } else {
                        DialogFragment exitDialog = ExitDialog.newInstance(BatteryFragment.this);
                        exitDialog.show(getFragmentManager(), "exitDialog");
                    }
                    return;
                }

                showOptimizationProgress();

                mPresenter.setPowerSavingMode(getActivity());

                Animation fadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);

                mOptimizeButton.startAnimation(fadeInAnimation);
                fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //mOptimizeButton.setVisibility(View.GONE);
                        mOptimizeButton.setClickable(false);
                        mOptimizeButton.setText(getResources().getString(R.string.loading));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    private void setWarningIconClickListener() {
        mWarningIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment recomendationsDialog = RecomendationsDialog.newInstance(BatteryFragment.this);
                recomendationsDialog.show(getChildFragmentManager(), recomendationsDialog.getClass().getSimpleName());
            }
        });
    }

    @Override
    public void onRecomendationsDialogClose() {
        mPresenter.getIssues();
    }

    public void showOptimizationProgress() {

        mProgress = 0;

        changeProgressDescription(R.string.cleaning_memory);
        showOptimizationProgressView();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mProgress == 100) {
                    hideOptimization_progress();
                    timer.cancel();
                } else {
                    mProgress++;
                    optimization_progressBar.setProgress(mProgress);
                    if (mProgress == 15) {
                        changeProgressDescription(R.string.reducing_brightness);
                    } else if (mProgress == 30) {
                        changeProgressDescription(R.string.disabling_wifi);
                    } else if (mProgress == 60) {
                        changeProgressDescription(R.string.disabling_bluetooth);
                    } else if (mProgress == 80) {
                        changeProgressDescription(R.string.disabling_rotation);
                    } else if (mProgress == 90) {
                        changeProgressDescription(R.string.enabling_silent_mode);
                    }
                }
            }
        }, 500, 30);


        Animation fadein = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        final Animation fadein2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        final Animation fadein3 = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        final Animation fadein4 = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        final Animation fadein5 = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        final Animation fadein6 = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        final Animation fadein7 = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        final Animation fadein8 = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);

        img1.startAnimation(fadein);
        fadein.setAnimationListener(new AnimationListener() {


            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img1.setVisibility(View.INVISIBLE);
                img2.startAnimation(fadein2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }


        });

        fadein2.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img2.setVisibility(View.INVISIBLE);
                img3.startAnimation(fadein3);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadein3.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img3.setVisibility(View.INVISIBLE);
                img4.startAnimation(fadein4);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        fadein4.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img4.setVisibility(View.INVISIBLE);
                img5.startAnimation(fadein5);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        fadein5.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img5.setVisibility(View.INVISIBLE);
                img6.startAnimation(fadein6);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadein6.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img6.setVisibility(View.INVISIBLE);
                img7.startAnimation(fadein7);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadein7.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img7.setVisibility(View.INVISIBLE);
                img8.startAnimation(fadein8);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        fadein8.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img8.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void hideOptimization_progress() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //optimization_progressBar.setVisibility(View.GONE);
                DialogFragment exitDialog = DoneDialog.newInstance(BatteryFragment.this, mGained.getText().toString().replace("Got", ""));
                exitDialog.show(getFragmentManager(), "exitDialog");
            }
        });
    }


    private void changeProgressDescription(final int description) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                optimization_textView.setText(description);
            }
        });
    }

    private void showOptimizationProgressView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                optimization_progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onAppExit() {
        getActivity().finish();
    }

    @Override
    public void onExitCancel() {

    }

    @Override
    public void onDoneClick() {
        Animation bounce = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
        ((LinearLayout) mHours.getParent()).startAnimation(bounce);
        mHours.startAnimation(bounce);
        mMinutes.startAnimation(bounce);
        mOptimizeButton.setText(getResources().getString(R.string.exit));
        mOptimizeButton.setClickable(true);
    }

}
