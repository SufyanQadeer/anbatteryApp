package com.serialmmf.Anbattery.dialogs;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.serialmmf.Anbattery.R;
import com.serialmmf.Anbattery.model.PowerManager;

/**
 * Created by juancarlos on 4/3/16.
 */
public class RecomendationsDialog extends DialogFragment implements OnClickListener {

    private PowerManager mPowerManager;

    private RecomendationsDialogListener mCallback;

    public interface RecomendationsDialogListener {
        void onRecomendationsDialogClose();
    }

    public static DialogFragment newInstance(RecomendationsDialogListener listener) {
        RecomendationsDialog dialog = new RecomendationsDialog();

        dialog.setListener(listener);

        Bundle args = new Bundle();
        dialog.setArguments(args);

        return dialog;
    }

    private void setListener(RecomendationsDialogListener listener) {
        mCallback = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_recomendations, container, false);

        getDialog().getWindow().requestFeature(STYLE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCancelable(false);

        mPowerManager = PowerManager.getInstance(getActivity());

        View disableData = view.findViewById(R.id.disable_data_button);
        View enableAirplane = view.findViewById(R.id.enable_airplane_mode_button);
        View disableGps = view.findViewById(R.id.disable_gps_button);

        if (!mPowerManager.isMobileDataEnabled()) {
            ((ViewGroup)disableData.getParent()).setVisibility(View.GONE);
        }

        if (mPowerManager.isAirplaneModeOn()) {
            ((ViewGroup)enableAirplane.getParent()).setVisibility(View.GONE);
        }

        if (!mPowerManager.isGpsEnabled()) {
            ((ViewGroup)disableGps.getParent()).setVisibility(View.GONE);
        }

        disableData.setOnClickListener(this);
        enableAirplane.setOnClickListener(this);
        disableGps.setOnClickListener(this);
        view.findViewById(R.id.accept_button).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.disable_data_button:
                mPowerManager.sendMobileDataIntent();
                break;
            case R.id.enable_airplane_mode_button:
                mPowerManager.sendAirplaneIntent();
                break;
            case R.id.disable_gps_button:
                mPowerManager.sendLocationIntent();
                break;
            case R.id.accept_button:
                if (mCallback != null) {
                    mCallback.onRecomendationsDialogClose();
                }

                getDialog().dismiss();
                break;
        }
    }
}
