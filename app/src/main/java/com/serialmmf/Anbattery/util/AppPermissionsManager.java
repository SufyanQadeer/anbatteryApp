package com.serialmmf.Anbattery.util;


import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by juancarlos on 26/8/16.
 */
public class AppPermissionsManager {

    public static final int ANBATTERY_PERMISSIONS_REQUEST = 1;

    private AppCompatActivity mActivity;

    private AppPermissionsManager(AppCompatActivity activity) {
        mActivity = activity;
    }


    public static boolean grantPermission(AppCompatActivity activity, String[] permissions) {
        if (VERSION.SDK_INT < VERSION_CODES.M) {
            return true;
        }

        return new AppPermissionsManager(activity).request(permissions);
    }

    @TargetApi(VERSION_CODES.M)
    private boolean request(String[] permissions) {
        ArrayList<String> permissionsList = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }
        }

        if (permissionsList.size() == 0) {
            return true;
        }

        mActivity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), ANBATTERY_PERMISSIONS_REQUEST);
        return false;
    }
}
