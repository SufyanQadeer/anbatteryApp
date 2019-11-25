package com.serialmmf.Anbattery.model;

/**
 * Created by juancarlos on 1/3/16.
 */
public interface StorageObserver {
    void onStorageDataUpdated(Long... values);

    void onStorageScanFinished(long cacheSize, long navCacheSize);
}
