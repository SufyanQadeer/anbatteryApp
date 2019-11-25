package com.serialmmf.Anbattery.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by juancarlos on 26/2/16.
 */
public class RAMMemoryInfo {

    private Activity mActivity;
    private long mTotalMemory;
    private long mFreeMemory;

    public RAMMemoryInfo(Activity activity) {
        mActivity = activity;
        mTotalMemory = getTotalMemory();
        updateMemoryStatus();
    }

    public long getFreeMem() {
        return mFreeMemory;
    }

    public long getTotalMem() {
        return mTotalMemory;
    }

    private void updateMemoryStatus() {

        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) mActivity.getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryInfo(memInfo);

        long availMem = memInfo.availMem;
        float f = availMem;
        float f1 = mTotalMemory;

        mFreeMemory = availMem;
        //Todo: implement this in the SystemListAdapter
//        int i = (int) ((f / f1) * 100F);
//        if (i != 0) {
//            String s = formatMemSize(availMem, 0);
//            txtFreeMemory.setText(s);
//            String s1 = formatMemSize(totalMemory, 0);
//            txtTotalMemory.setText(s1);
//            String s2 = formatMemSize(totalMemory - availMem, 0);
//            txtUsedMemory.setText(s2);
//            setPercentage(i);// Progress Wheel progress percentage
//        }

        //Todo: revise this
//        pref = getActivity().getSharedPreferences(
//                "last_boost", Context.MODE_PRIVATE);
//        String lastBoost = pref.getString("boost_time", "");
//        String lastMemoryCleaned = pref.getString("memory_cleaned", "");
//        String lastCahchedCleaned = pref.getString("cache_cleaned", "");

        // Get Boost Information From SharedPreference and Display
    }

    private long getTotalMemory() {

        /**
         * The entries in the /proc/meminfo can help explain
         * what's going on with your memory usage
         */

        String str1 = "/proc/meminfo";
        String str2 = "tag";
        String[] arrayOfString;
        long initial_memory = 0, free_memory = 0;

        try {

            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);

            for (int i = 0; i < 2; i++) {
                str2 = str2 + " " + localBufferedReader.readLine();
            }

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");// show memory info into log
            }

            // total Memory
            initial_memory = Integer.valueOf(arrayOfString[2]).intValue();
            free_memory = Integer.valueOf(arrayOfString[5]).intValue();

            Log.d("MEM", "FREE " + (free_memory / 1024) + " MB");
            Log.d("MEM", "INIT " + (initial_memory * 1024L) + " MB");

            localBufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return (initial_memory * 1024L);
    }
}
