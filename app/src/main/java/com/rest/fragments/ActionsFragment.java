package com.rest.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import com.rest.R;

/**
 * Created on 24/01/2017
 */

public class ActionsFragment extends android.support.v4.app.Fragment {

    public interface OnTimePickedListener {
        public void onPick(int hour, int minute);
    }

    Button fixedWakeup, fixedBedtime, repeatingAlarms, settings;
    OnTimePickedListener listener;

    public void setListener(OnTimePickedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.actions_fragment, container, false);


        fixedWakeup = (Button) root.findViewById(R.id.fixed_wakeup);
        fixedWakeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        fixedBedtime = (Button) root.findViewById(R.id.fixed_bedtime);
        repeatingAlarms = (Button) root.findViewById(R.id.repeating_alarms);
        settings = (Button) root.findViewById(R.id.settings);
        return root;
    }

    private void showTimePicker() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.time_picker, null);
        builder.setView(root);
        final TimePicker picker = (TimePicker) root.findViewById(R.id.timePicker);
        picker.setIs24HourView(true);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int hours, mins;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hours = picker.getHour();
                    mins = picker.getMinute();
                } else {
                    hours = picker.getCurrentHour();
                    mins = picker.getCurrentMinute();
                }

                listener.onPick(hours, mins);
            }
        });

        builder.create().show();
    }
}
