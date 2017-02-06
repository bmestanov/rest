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

import java.util.Calendar;

/**
 * Created on 24/01/2017
 */

public class ActionsFragment extends Fragment {

    Button fixedWakeup, fixedRestTime, repeatingAlarms, settings;
    OnActionSelectedListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_actions, container, false);


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
        repeatingAlarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRepeatedAlarmsPicked();
            }
        });
        settings = (Button) root.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSettingsPicked();
            }
        });
        return root;
    }

    public void setListener(OnActionSelectedListener listener) {
        this.listener = listener;
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
                    listener.onFixedRestPicked(hours, mins);
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
                            c.get(Calendar.MINUTE));
                }
            });
        }

        builder.create().show();
    }

    public interface OnActionSelectedListener {
        public void onFixedAlarmPicked(int hour, int minute);

        public void onFixedRestPicked(int hour, int minute);

        public void onRepeatedAlarmsPicked();

        public void onSettingsPicked();
    }
}
