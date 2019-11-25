package com.serialmmf.Anbattery.dialogs;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.serialmmf.Anbattery.R;

/**
 * Created by juancarlos on 4/3/16.
 */
public class KillDialog extends DialogFragment implements OnClickListener {

    private KillDialogListener mCallback;

    public interface KillDialogListener {
        void onKill();

        void onKillCanceled();
    }

    public KillDialog() {

    }

    public static DialogFragment newInstance(KillDialogListener listener, String proccesName) {
        KillDialog dialog = new KillDialog();
        Bundle args = new Bundle();
        args.putString("proccesName", proccesName);
        dialog.setArguments(args);

        dialog.setListener(listener);

        return dialog;
    }

    private void setListener(KillDialogListener listener) {
        mCallback = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_kill, container, false);

        getDialog().getWindow().requestFeature(STYLE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCancelable(false);

        ((TextView)view.findViewById(R.id.title_textView)).setText(getArguments().getString("proccesName"));
        view.findViewById(R.id.ok_button).setOnClickListener(this);
        view.findViewById(R.id.cancel_button).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_button:
                mCallback.onKill();
                break;
            case R.id.cancel_button:
                mCallback.onKillCanceled();
                break;
        }

        getDialog().dismiss();
    }
}
