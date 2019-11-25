package com.serialmmf.Anbattery.model;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.widget.CheckBox;

import com.serialmmf.Anbattery.util.MemoryStringFormater;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

public class Task {

	private ApplicationInfo appinfo;
	public long mem;
	private java.lang.Package pkgInfo;
	private PackageManager pm;
	private ActivityManager.RunningAppProcessInfo runinfo;
	private String title;
	private CheckBox chkTask;
	private boolean mChecked;
	private Context mContext;
	private PackageStats mPackageStats;
    private int mPid;

	public Task(Context context,
                PackageStats pStats) {
		mContext = context;
		this.appinfo = null;
		this.pkgInfo = null;
		this.title = null;
		mPackageStats = pStats;
		PackageManager pkm = context.getApplicationContext()
				.getPackageManager();
		this.pm = pkm;
	}

	public Task(Context context,
                ActivityManager.RunningAppProcessInfo runinfo) {
		mContext = context;
		this.appinfo = null;
		this.pkgInfo = null;
		this.title = null;
		this.runinfo = runinfo;
		PackageManager pkm = context.getApplicationContext()
				.getPackageManager();
		this.pm = pkm;
	}

	public Task(Context context, ApplicationInfo appinfo) {

		this.appinfo = null;
		this.pkgInfo = null;
		this.runinfo = null;
		this.title = null;
		this.appinfo = appinfo;
		PackageManager pkm = context.getApplicationContext()
				.getPackageManager();
		this.pm = pkm;
	}

	public void getAppInfo(Package pkInfo) {

		if (appinfo == null) {
			try {
				String s = runinfo.processName;
				this.appinfo = pm.getApplicationInfo(s, PackageManager.GET_META_DATA);
			} catch (Exception e) {
			}
		}
	}

	public int getIcon() {
		return appinfo.icon;
	}

	public Drawable getIconDrawable() {
		return appinfo.loadIcon(pm);
	}

	public String getPackageName() {
		return appinfo.packageName;
	}

	public String getTitle() {
		if (mPackageStats != null) {
			return mPackageStats.packageName;
		}

		if (title == null) {
			try {
				this.title = appinfo.loadLabel(pm).toString();
			} catch (Exception e) {

			}
		}
		return title;
	}

	public void setMem(int mem) {
		this.mem = mem;
	}

	public boolean isGoodProcess() {

		if (appinfo != null) {
			return true;
		} else {
			return false;
		}
	}

    public void setPid(int pid) {
        mPid = pid;
    }

	public ApplicationInfo getAppinfo() {
		return appinfo;
	}

	public void setAppinfo(ApplicationInfo appinfo) {
		this.appinfo = appinfo;
	}

	public java.lang.Package getPkgInfo() {
		return pkgInfo;
	}

	public void setPkgInfo(java.lang.Package pkgInfo) {
		this.pkgInfo = pkgInfo;
	}

	public ActivityManager.RunningAppProcessInfo getRuninfo() {
		return runinfo;
	}

	public void setRuninfo(ActivityManager.RunningAppProcessInfo runinfo) {
		this.runinfo = runinfo;
	}

	public boolean isChecked() {
		return mChecked;
	}

	public void setChecked(boolean checked) {
		mChecked = checked;
	}

	public void setChkTask(CheckBox chkTask) {
		this.chkTask = chkTask;
	}

	public String getMemUsage() {
		return MemoryStringFormater.formatMemSize(mem, 0);
	}

	public String getCPUUsage() {
		return new StringBuilder(String.valueOf(new DecimalFormat("#.##")
				.format(getCPUTime(mPid)))).append("%").toString();
	}

	public double getCPUTime(int pid) {

		double d = 0;
		String path = (new StringBuilder("/proc/")).append(pid).append("/stat")
				.toString();
		File file = new File(path);
		DataInputStream dis = null;
		try {
			FileInputStream is = new FileInputStream(file);
			dis = new DataInputStream(is);
			String[] as = dis.readLine().split("\\s+");
			int j = 13;
			while (j < 17) {
				int k = Integer.parseInt(as[j]);
				double dl = k;
				d += dl;
				j++;
			}

			d /= 1000D;
			while (d > 10D) {
				d /= 10D;
			}

			if (d < 0.01D) {
				if (d == 0D) {
					d = (double) ((new Random()).nextInt(22) + 3) / 100D;
					return (double) (int) (d * 0F) / 100D;
				}
				d *= 10D;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return d;
	}

}
