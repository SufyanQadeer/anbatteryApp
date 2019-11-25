package com.serialmmf.Anbattery.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class InternalMemoryInfo {

	private static Context context;
	private static final Pattern DIR_SEPORATOR = Pattern.compile("/");

	public InternalMemoryInfo(Context context) {
		this.context = context;
	}

	public static boolean externalMemoryAvailable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return (availableBlocks * blockSize);
	}

	public static long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return (totalBlocks * blockSize);
	}

	public static String getFreeInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long total = (long) stat.getBlockCount() * (long) stat.getBlockSize();
		long available = (long) stat.getAvailableBlocks()
				* (long) stat.getBlockSize();
		long free = Math.abs(total - available);
		return Formatter.formatFileSize(context, free);
	}

	public static long getAvailableExternalMemorySize(File path) {
		if (externalMemoryAvailable()) {
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return (availableBlocks * blockSize);
		} else {
			return 0L;
		}
	}

	public static long getTotalExternalMemorySize(File path) {
		if (externalMemoryAvailable()) {
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return (totalBlocks * blockSize);
		} else {
			return 0L;
		}
	}

	public static String getFreeExternalMemorySize(File path) {
		if (externalMemoryAvailable()) {
			StatFs stat = new StatFs(path.getPath());
			long total = (long) stat.getBlockCount()
					* (long) stat.getBlockSize();
			long available = (long) stat.getAvailableBlocks()
					* (long) stat.getBlockSize();
			long free = 0L;
			free = Math.abs(total - available);
			return Formatter.formatFileSize(context, free);
		} else {
			return "Error";
		}
	}

	public static String[] getStorageDirectories() {
		// Final set of paths
		final Set<String> rv = new HashSet<String>();
		// Primary physical SD-CARD (not emulated)
		final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
		// All Secondary SD-CARDs (all exclude primary) separated by ":"
		final String rawSecondaryStoragesStr = System
				.getenv("SECONDARY_STORAGE");
		// Primary emulated SD-CARD
		final String rawEmulatedStorageTarget = System
				.getenv("EMULATED_STORAGE_TARGET");
		if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
			// Device has physical external storage; use plain paths.
			if (TextUtils.isEmpty(rawExternalStorage)) {
				// EXTERNAL_STORAGE undefined; falling back to default.
				// rv.add("/storage/sdcard0");
			} else {
				// rv.add(rawExternalStorage);
			}
		} else {
			// Device has emulated storage; external storage paths should have
			// userId burned into them.
			final String rawUserId;
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
				rawUserId = "";
			} else {
				final String path = Environment.getExternalStorageDirectory()
						.getAbsolutePath();
				final String[] folders = DIR_SEPORATOR.split(path);
				final String lastFolder = folders[folders.length - 1];
				boolean isDigit = false;
				try {
					Integer.valueOf(lastFolder);
					isDigit = true;
				} catch (NumberFormatException ignored) {
				}
				rawUserId = isDigit ? lastFolder : "";
			}
			// /storage/emulated/0[1,2,...]
			if (TextUtils.isEmpty(rawUserId)) {
				// rv.add(rawEmulatedStorageTarget);
			} else {
				// rv.add(rawEmulatedStorageTarget + File.separator +
				// rawUserId);
			}
		}
		// Add all secondary storages
		if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
			// All Secondary SD-CARDs splited into array
			final String[] rawSecondaryStorages = rawSecondaryStoragesStr
					.split(File.pathSeparator);
			Collections.addAll(rv, rawSecondaryStorages);
		}
		return rv.toArray(new String[rv.size()]);
	}
}
