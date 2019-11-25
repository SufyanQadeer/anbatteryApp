package com.serialmmf.Anbattery.dialogs;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.serialmmf.Anbattery.R;

public class DoneDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = ExitDialog.class.getSimpleName();
    private DoneDialogListener mCallback;
    String msg="";
    public interface DoneDialogListener {


        void onDoneClick();
    }

    public DoneDialog() {

    }

    public static DialogFragment newInstance(DoneDialogListener listener, String msg) {
        DoneDialog dialog = new DoneDialog();
        dialog.msg=msg;
        dialog.setListener(listener);

        return dialog;
    }

    private void setListener(DoneDialogListener listener) {
        mCallback = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_done, container, false);

        getDialog().getWindow().requestFeature(STYLE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCancelable(false);

        view.findViewById(R.id.ok_button).setOnClickListener(this);
//        TextView content_remain=(TextView)view.findViewById(R.id.content_remain);
//        content_remain.setText(msg);
//        content_remain.setVisibility(View.GONE);

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
                mCallback.onDoneClick();
            }
        }

        getDialog().dismiss();
    }
}
