package com.serialmmf.Anbattery;

import android.app.Activity;
import android.app.Application;

public class AppController extends Application {


    private static AppController mInstance;
    private Activity mActivity;

    public static synchronized AppController getInstance() {
        if (mInstance == null) {
            mInstance = new AppController();
        }

        return mInstance;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public Activity getActivity() {
        return mActivity;
    }
}
