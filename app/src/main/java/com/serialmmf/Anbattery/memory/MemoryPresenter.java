package com.serialmmf.Anbattery.memory;

import android.app.Activity;

import com.serialmmf.Anbattery.dialogs.KillDialog.KillDialogListener;
import com.serialmmf.Anbattery.model.MemoryDataManager;
import com.serialmmf.Anbattery.model.Task;

/**
 * Created by juancarlos on 7/3/16.
 */
public class MemoryPresenter implements KillDialogListener
{

    private MemoryView mView;
    private Task mTask;
    private int mItemPosition;
    private Activity mActivity;

    public MemoryPresenter(MemoryView view, Activity activity) {
        mView = view;
        mActivity = activity;
    }

    public void itemSwiped(Task task, int itemPosition) {
        mTask = task;
        mItemPosition = itemPosition;

        if (MemoryDataManager.getInstance().isImportant(mTask.getPackageName())) {
            mView.showKillDialog(mTask.getTitle());
        } else {
            kill();
        }
    }

    public void kill() {
        mTask.setChecked(true);
        MemoryDataManager.getInstance().killTask(mActivity, mTask);
        mView.removeItem(mTask, mItemPosition);
    }

    @Override
    public void onKill() {
        kill();
    }

    @Override
    public void onKillCanceled() {
        mTask = null;
        mView.restoreItem();
    }
}
