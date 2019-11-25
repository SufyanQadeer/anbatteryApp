package com.serialmmf.Anbattery.model;

import java.util.ArrayList;

/**
 * Created by juancarlos on 1/3/16.
 */
public interface MemoryObserver {
    void onMemoryDataUpdated(Integer... values);

    void onMemoryScanFinished(ArrayList<Task> tasksList, long storageSize);
}
