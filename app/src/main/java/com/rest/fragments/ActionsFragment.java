package com.rest.fragments;

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

import java.util.Calendar;

/**
 * Created on 24/01/2017
 */

public class ActionsFragment extends android.support.v4.app.Fragment {

    public interface OnActionSelectedListener {
        public void onFixedAlarmPicked(int hour, int minute);

        //Rest notification is optional - user can select to rest now
        public void onFixedRestPicked(int hour, int minute, boolean notify);

        public void onRepeatedAlarmsPicked();

        public void onSettingsPicked();
    }

    Button fixedWakeup, fixedRestTime, repeatingAlarms, settings;
    OnActionSelectedListener listener;

    public void setListener(OnActionSelectedListener listener) {
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
                showTimePicker(false);
            }
        });

        fixedRestTime = (Button) root.findViewById(R.id.fixed_bedtime);
        fixedRestTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(true);
            }
        });

        repeatingAlarms = (Button) root.findViewById(R.id.repeating_alarms);
        settings = (Button) root.findViewById(R.id.settings);
        return root;
    }

    private void showTimePicker(final boolean fixedRestTime) {
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

                if (fixedRestTime) {
                    listener.onFixedRestPicked(hours, mins, true);
                } else {
                    listener.onFixedAlarmPicked(hours, mins);
                }
            }
        });

        if (fixedRestTime) {
            builder.setNeutralButton(R.string.now, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Calendar c = Calendar.getInstance();
                    listener.onFixedRestPicked(c.get(Calendar.HOUR_OF_DAY),
                            c.get(Calendar.MINUTE), false);
                }
            });
        }

        builder.create().show();
    }
}
