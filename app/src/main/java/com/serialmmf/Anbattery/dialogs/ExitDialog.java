package com.serialmmf.Anbattery.dialogs;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.serialmmf.Anbattery.R;

/**
 * Created by juancarlos on 4/3/16.
 */
public class ExitDialog extends DialogFragment implements OnClickListener {

    private static final String TAG = ExitDialog.class.getSimpleName();
    private ExitDialogListener mCallback;

    public interface ExitDialogListener {
        void onAppExit();

        void onExitCancel();
    }

    public ExitDialog() {

    }

    public static DialogFragment newInstance(ExitDialogListener listener) {
        ExitDialog dialog = new ExitDialog();

        dialog.setListener(listener);

        return dialog;
    }

    private void setListener(ExitDialogListener listener) {
        mCallback = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_exit, container, false);

        getDialog().getWindow().requestFeature(STYLE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCancelable(false);

        view.findViewById(R.id.ok_button).setOnClickListener(this);
        view.findViewById(R.id.cancel_button).setOnClickListener(this);

        return view;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            Log.i(TAG, "Exception showing " + e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        if (mCallback != null) {
            if (v.getId() == R.id.ok_button) {
                mCallback.onAppExit();
            } else {
                mCallback.onExitCancel();
            }
        }

        getDialog().dismiss();
    }
}
