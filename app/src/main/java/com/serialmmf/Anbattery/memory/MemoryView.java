package com.serialmmf.Anbattery.memory;

import com.serialmmf.Anbattery.model.Task;

/**
 * Created by juancarlos on 7/3/16.
 */
public interface MemoryView {
    void showKillDialog(String taskName);

    void removeItem(Task task, int position);

    void restoreItem();
}
