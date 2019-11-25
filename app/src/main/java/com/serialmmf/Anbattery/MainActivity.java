package com.serialmmf.Anbattery;

import android.Manifest;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.navigation.NavigationView;
import com.serialmmf.Anbattery.dialogs.ExitDialog;
import com.serialmmf.Anbattery.dialogs.WizardDialog;
import com.serialmmf.Anbattery.model.PowerManager;
import com.serialmmf.Anbattery.receivers.AutomaticOptimizationReceiver;
import com.serialmmf.Anbattery.receivers.PowerConnectedReceiver;
import com.serialmmf.Anbattery.services.BatteryService;
import com.serialmmf.Anbattery.util.PrefsManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ExitDialog.ExitDialogListener,
        WizardDialog.WizardDialogListener {


    private final static String TAG = "MainActivity";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static final int REQUEST_NOTIFICATIONS_POLICY = 2;
    public static final int REQUEST_WRITE_SETTINGS = 3;

    private FragmentManager mFragmentManager;
    private InterstitialAd mInterstitialAd;
    private boolean mIsClean;
    private boolean mMemoryWizardShowed;
    private boolean mSystemWizardShowed;
    private boolean mIsWorking;
    private boolean mIsInitialiced;
    private Handler mRevealHandler;
    private boolean mShowExitDialog;
    private FragmentPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private boolean mHasWriteSettingsPermissions;

    NavController navController;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        requestNeededPermissions();

        initApp();

        initViews();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                if(menuItem.getItemId() == R.id.nav_review)
                {

                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);


                    try
                    {

                        startActivity(goToMarket);

                    } catch (ActivityNotFoundException e)
                    {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));

                    }

                }
                else  if(menuItem.getItemId() == R.id.nav_settings)
                {
                    navController.navigate(R.id.nav_settings);
                }
                else  if(menuItem.getItemId() == R.id.nav_home)
                {
                    navController.navigate(R.id.nav_home);
                }

                return false;
            }
        });

        Intent service_intent = new Intent(this, BatteryService.class);
        startService(service_intent);

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }



    private void requestNeededPermissions() {
        if (writeSettingsPermissionNeeded()) {
            requestWriteSettingsPermissions();
        } else if (notificationPolicyPermissionNeeded()) {
            requestNotificationServicePermisssion();
        }
    }

    private boolean writeSettingsPermissionNeeded()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(this);
    }

    private boolean notificationPolicyPermissionNeeded() {
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);

        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !notificationManager.isNotificationPolicyAccessGranted();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String action = getIntent().getAction();

        if (action != null)
        {
            if (action.equals(PowerConnectedReceiver.ACTION_SMART_CHARGE))
            {
                PowerManager.getInstance(this).startSmartCharge(this);
            } else if (action.equals(AutomaticOptimizationReceiver.ACTION_AUTOMATIC_OPTIMIZATION)) {
                PowerManager.getInstance(this).startAutomaticOptimization(this);
            }
        }


//        if (mShowExitDialog) {
//            try {
//                DialogFragment exitDialog = ExitDialog.newInstance(this);
//                exitDialog.show(mFragmentManager, "exitDialog");
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//            }
//        }

        mShowExitDialog = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() && mRevealHandler != null) {
            mRevealHandler.removeCallbacksAndMessages(null);
        }


    }

    @Override
    public void onBackPressed() {
        try {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();

                        DialogFragment exitDialog = ExitDialog.newInstance(MainActivity.this);
                        exitDialog.show(getSupportFragmentManager(), "exitDialog");
                    }
                });
                mInterstitialAd.show();
            } else {
                DialogFragment exitDialog = ExitDialog.newInstance(this);
                exitDialog.show(getSupportFragmentManager(), "exitDialog");
            }
        } catch (IllegalStateException e) {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
       /* switch (v.getId()) {
            case R.id.tab1_button:
                mViewPager.setCurrentItem(0, false);
                showWizardDialog(R.string.welcome_text, 100);
                trackScreen("BatteryFragment");
                break;
            case R.id.tab2_button:
                mViewPager.setCurrentItem(1, false);
                showWizardDialog(R.string.task_assistant, 100);
                trackScreen("MemoryFragment");
                break;
            case R.id.tab3_button:
                mViewPager.setCurrentItem(2, false);
                showWizardDialog(R.string.option_assistant, 100);
                trackScreen("OptionFragment");
                break;
        }*/
    }


    private void initApp() {

        AppController appController = AppController.getInstance();
        appController.setActivity(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mMemoryWizardShowed = false;
        mSystemWizardShowed = false;

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interistitial_id));

        mInterstitialAd.loadAd(adRequest);
    }

    private void checkPermissions()
    {
        int permissionReadPhoneState = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        int permissionWriteExtStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionReadPhoneState != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (permissionWriteExtStorage != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
        }

    }

    private void initViews() {


        showWizardDialog(R.string.welcome_text, 200);
    }


    private boolean isPreNougat() {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.N);
    }

    @Override
    public void onAppExit() {
        finish();
    }

    @Override
    public void onExitCancel() {

    }


    private void requestWriteSettingsPermissions() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.MyDialogTheme)
                .setTitle(R.string.important)
                .setCancelable(false)
                .setMessage(R.string.need_write_seettings)
                .setPositiveButton(R.string.accept,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {

                                startWriteSettingsManager(dialog);

                            }
                        });

        AlertDialog dialog = builder.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccentTerciary));


    }

    private void startWriteSettingsManager(DialogInterface dialog) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_WRITE_SETTINGS);

        dialog.dismiss();
    }

    private void requestNotificationServicePermisssion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.MyDialogTheme)
                .setTitle(R.string.important)
                .setCancelable(false)
                .setMessage(R.string.need_notifications_policy_permission)
                .setPositiveButton(R.string.accept,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                startPolicyActionManager(dialog);
                            }
                        });

        AlertDialog dialog = builder.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccentTerciary));
    }

    private void startPolicyActionManager(DialogInterface dialog) {
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
        startActivityForResult(intent, REQUEST_NOTIFICATIONS_POLICY);

        dialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_WRITE_SETTINGS || requestCode == REQUEST_NOTIFICATIONS_POLICY) {
            requestNeededPermissions();
        }
    }

    private void showWizardDialog(int textResource, int delay) {

        if (wizardShowed(textResource)) {
            return;
        }

        final String text = getString(textResource);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DialogFragment dialog = WizardDialog.newInstance(MainActivity.this, text);
                dialog.show(getSupportFragmentManager(), "wizardDialog");
                checkPermissions();
            }
        }, delay);
    }

    private boolean wizardShowed(int textResource) {
        if (textResource == R.string.task_assistant) {
            if (!PrefsManager.getInstance(this).showTaskAssistant()) {
                return true;
            } else {
                PrefsManager.getInstance(this).setTaskAssistanStatus(false);
            }
        } else if (textResource == R.string.option_assistant) {
            if (!PrefsManager.getInstance(this).showOptionAssistant()) {
                return true;
            } else {
                PrefsManager.getInstance(this).setOptionAssistanStatus(false);
            }
        } else if (textResource == R.string.welcome_text) {
            if (!PrefsManager.getInstance(this).showBatteryAssistant()) {
                return true;
            } else {
                PrefsManager.getInstance(this).setBatteryAssistanStatus(false);
            }
        }

        return false;
    }

    @Override
    public void onDialogClose() {

    }


}
