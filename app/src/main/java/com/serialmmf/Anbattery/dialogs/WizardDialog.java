package com.serialmmf.Anbattery.dialogs;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.serialmmf.Anbattery.R;
import com.serialmmf.Anbattery.util.PrefsManager;

/**
 * Created by juancarlos on 4/3/16.
 */
public class WizardDialog extends DialogFragment implements OnCheckedChangeListener, OnClickListener
{

    private WizardDialogListener mCallback;
    private String mText;

    public interface WizardDialogListener {
        void onDialogClose();
    }

    public WizardDialog() {

    }

    public static DialogFragment newInstance(WizardDialogListener listener, String text) {
        WizardDialog dialog = new WizardDialog();

        dialog.setListener(listener);

        Bundle args = new Bundle();
        args.putString("text", text);
        dialog.setArguments(args);

        return dialog;
    }

    private void setListener(WizardDialogListener listener) {
        mCallback = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_wizard, container, false);

        getDialog().getWindow().requestFeature(STYLE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCancelable(false);

        mText = getArguments().getString("text");

        ((CheckBox)view.findViewById(R.id.show_welcome_checkbox)).setOnCheckedChangeListener(this);

        ((TextView)view.findViewById(R.id.content_textView)).setText(mText);

        view.findViewById(R.id.button).setOnClickListener(this);

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mText.equals(getString(R.string.task_assistant))) {
            PrefsManager.getInstance(getActivity()).setTaskAssistanStatus(isChecked);
        } else if (mText.equals(getString(R.string.option_assistant))) {
            PrefsManager.getInstance(getActivity()).setOptionAssistanStatus(isChecked);
        } else {
            PrefsManager.getInstance(getActivity()).setBatteryAssistanStatus(isChecked);
        }
    }

    @Override
    public void onClick(View v) {
        getDialog().dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mCallback.onDialogClose();

        super.onDismiss(dialog);
    }
}