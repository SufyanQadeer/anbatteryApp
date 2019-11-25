package com.serialmmf.Anbattery.model;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;

import com.serialmmf.Anbattery.util.AppShort;
import com.serialmmf.Anbattery.util.PrefsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by juancarlos on 1/3/16.
 */
public class MemoryDataManager {

    private static final String TAG = "MemoryDataManager";
    private static MemoryDataManager managerInstance = null;
    private int mMemory;
    private ArrayList<Task> mTasks;
    private ArrayList<MemoryObserver> mObservers;
    private boolean mIsProcessing;

    private MemoryDataManager() {
        mTasks = new ArrayList<>();
        mObservers = new ArrayList<>();
    }

    public static MemoryDataManager getInstance() {
        if (managerInstance == null) {
            managerInstance = new MemoryDataManager();
        }

        return managerInstance;
    }

    public void addObserver(MemoryObserver observer) {
        mObservers.add(observer);
    }

    public void notifyProgressToObservers(Integer... values) {
        for (MemoryObserver observer : mObservers) {
            if (observer != null) {
                observer.onMemoryDataUpdated(values);
            }
        }
    }

    public void notifyResultToObservers(ArrayList<Task> taskList) {
        for (MemoryObserver observer : mObservers) {
            if (observer != null) {
                observer.onMemoryScanFinished(taskList, mMemory);
            }
        }
    }

    public ArrayList<Task> getMemoryData(Activity activity) {
        if (!mIsProcessing) {
            new GetTask(activity).execute();
        }
        return mTasks;
    }

    public void startScan(Activity activity) {
        new GetTask(activity).execute();
    }

    public void cleanMemory(Activity activity, ArrayList tasks) {
            new RemoveProcceses(activity, tasks).execute();
    }

    public void killTask(Activity activity, Task task) {
        ArrayList<Task> taskToKill = new ArrayList<>();
        taskToKill.add(task);
        cleanMemory(activity, taskToKill);
    }

    public boolean isImportant(String pname) {

        if (pname.equals("android") || pname.equals("android.process.acore")
                || pname.equals("system") || pname.equals("com.android.phone")
                || pname.equals("com.android.systemui")
                || pname.equals("com.android.launcher")) {
            return true;
        } else {
            return false;
        }
    }

    public class GetTask extends AsyncTask<Void, Double, ArrayList<Task>> {
        private Activity activity;

        public GetTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            notifyProgressToObservers(mMemory, (int) Math.ceil(values[0]));
            Log.i(TAG, "Progress: " + (int) Math.ceil(values[0]) + "%");

        }

        @Override
        protected ArrayList<Task> doInBackground(Void... arg0) {
            mIsProcessing = true;

            ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            final PackageManager packageManager = activity.getApplicationContext().getPackageManager();
            List<ProcessManager.Process> apps = ProcessManager.getRunningApps();

            mMemory = 0;
            ArrayList<Task> arrList = new ArrayList<Task>();
            Package pInfo = new Package(activity);

            int tasks = Integer.MAX_VALUE;
            int readedTasks = 0;

            if (!PrefsManager.getInstance(activity).timeForNewClean()) {
                tasks = (int) (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) -
                        TimeUnit.MILLISECONDS.toSeconds(PrefsManager.getInstance(activity).lastCleanTime()))
                        / (60 * 6) - 1;
            }

            double progress = 0;
            double progressInc = 100 / (apps.size() * 1.0);



            for(ProcessManager.Process app : apps) {
                ApplicationInfo appInfo = null;
                try {
                    appInfo= app.getApplicationInfo(activity, 0);
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                    progress = progress + progressInc;
                    publishProgress(progress);
                }

                String s = app.getPackageName();

                Log.i(TAG, s);

                if (!s.contains(activity.getPackageName())) {

//                    if (runproInfo.importance == 130
//                            || runproInfo.importance == 300
//                            || runproInfo.importance == 100
//                            || runproInfo.importance == 400) {

                    Task info = new Task(activity, appInfo);
                    info.setPid(app.pid);
                    info.getAppInfo(pInfo);
                    if (!isImportant(s)) {
                        if (readedTasks > tasks) {
                            continue;
                        }

                        readedTasks++;

                        info.setChecked(true);
                    }

                    if (info.isGoodProcess()) {
                        int j = app.pid;
                        int i[] = new int[1];
                        i[0] = j;
                        Debug.MemoryInfo memInfo[] = am
                                .getProcessMemoryInfo(i);
                        int k = memInfo.length;
                        for (int l = 0; l < memInfo.length; l++) {
                            Debug.MemoryInfo mInfo = memInfo[l];
                            int m = mInfo.getTotalPss() * 1024;
                            info.setMem(m);
//                            int jl = mInfo.getTotalPss() * 1024;
//                            int kl = mMemory;
//                            if (jl > kl)
                            if (!isImportant(s)) {
                                mMemory += m;
                            }
                        }
                        //if (mMemory > 0)
                        arrList.add(info);
                    }
                }
            }

            progress = progress + progressInc;
            publishProgress(progress);
//            }

            publishProgress(100.0);
            return arrList;
        }

        @Override
        protected void onPostExecute(ArrayList<Task> arrList) {
            mIsProcessing = false;

            if (arrList != null) {

                try {
                    AppShort shortmem = new AppShort();
                    Collections.sort(arrList, shortmem);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

                mTasks = arrList;

                notifyResultToObservers(mTasks);
            }
        }

//        private void checkProgress() {
//            if (realProgress == totalProgress) {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        notifyResultToObservers(arrList);
//                    }
//                });
//            }
//        }
    }

    private class RemoveProcceses extends AsyncTask<Void, Double, Void> {
        private Activity activity;
        private ArrayList<Task> tasks;
        private boolean mustWait;
        private boolean mustRefresh;

        public RemoveProcceses(Activity activity, ArrayList<Task> tasks) {
            this.activity = activity;

            mustWait = true;

            if (tasks != null) {
                this.tasks = tasks;

                mustWait = false;
            } else {
                this.tasks = mTasks;
            }

            if (this.tasks.size() > 1) {
                mustRefresh = true;
                mTasks = new ArrayList<>();
            }
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            notifyProgressToObservers(0, (int) Math.ceil(values[0]));
        }

        @Override
        protected Void doInBackground(Void... params) {
            mIsProcessing = true;

            ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);

            double progress = 0;
            double progressInc = 100 / (tasks.size() * 1.0);

            try {
                for (Task task : tasks) {
                    if (task.isChecked()) {
                        Log.i("TaskList: ", task.getPackageName());
                        am.killBackgroundProcesses(task.getPackageName());
                    } else {
                        if (mustRefresh) {
                            mTasks.add(task);
                        }
                    }

                    progress = progress + progressInc;
                    publishProgress(progress);

                    if (mustWait) {
                        Thread.sleep(50);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            publishProgress(100.0);

            tasks = null;

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mustRefresh) {
                notifyResultToObservers(mTasks);
            }
        }

    }
}
