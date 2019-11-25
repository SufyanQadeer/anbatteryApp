package com.serialmmf.Anbattery.model;

import com.serialmmf.Anbattery.util.MemoryStringFormater;

/**
 * Created by juancarlos on 26/2/16.
 */
public class SystemItem {
    private String mtitle;
    private String mUsed;
    private String mFree;
    private String mTotal;
    private String mActive;
    private int mPercent;

    public SystemItem (String title) {
        mtitle = title;
    }

    public void setMemoryStatus(long total, long available) {
        mUsed = MemoryStringFormater.formatMemSize(total - available, 0);
        mFree = MemoryStringFormater.formatMemSize(available, 0);
        mTotal = MemoryStringFormater.formatMemSize(total, 0);
        mPercent = (int)(((float)available / (float)total) * 100F);
    }

    public String getTitle() {
        return mtitle;
    }

    public String getUsed() {
        return mUsed;
    }

    public String getFree() {
        return mFree;
    }

    public String getTotal() {
        return mTotal;
    }

    public String getActive() {
        return mActive;
    }

    public int getPercent() {
        return mPercent;
    }
}
