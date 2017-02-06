package com.rest.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import com.rest.R;
import com.rest.adapters.RepeatedAlarmAdapter;
import com.rest.models.Alarm;
import com.rest.state.App;

import java.util.List;

/**
 * Created on 06/02/2017
 */

public class RepeatingAlarmsFragment extends Fragment {
    public static final String REPTD_ALARMS_FRAGMENT =
            RepeatingAlarmsFragment.class.getSimpleName();

    RepeatedAlarmAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_repeated_alarms, container, false);

        root.findViewById(R.id.add_repeating_alarm_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addAlarm();
                    }
                });

        List<Alarm> currentAlarms = App.getState().getAlarms();

        adapter = new RepeatedAlarmAdapter(getActivity(), currentAlarms);

        if (currentAlarms.isEmpty()) {
            root.findViewById(R.id.no_repeating_alarms).setVisibility(View.VISIBLE);
            root.findViewById(R.id.alarmsList).setVisibility(View.GONE);
            return root;
        }


        ListView alarmsList = (ListView) root.findViewById(R.id.alarmsList);
        alarmsList.setAdapter(adapter);
        return root;
    }

    private void addAlarm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.RateDialog);

        View root = LayoutInflater.from(getActivity())
                .inflate(R.layout.repeated_time_picker, null);

        builder.setView(root);
        final TimePicker picker = (TimePicker) root.findViewById(R.id.timePicker);
        final LinearLayout daysLayout = (LinearLayout) root.findViewById(R.id.days_selector);

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

                Alarm.DaysOfWeek days = getSelectedDays(daysLayout);
                Log.d(REPTD_ALARMS_FRAGMENT, String.format("Time received: %d:%d %s",
                        hours,
                        mins,
                        days.toString(getActivity(), true)));

                if (!days.isRepeatSet()) {
                    // No days were selected. Not okay!
                    return; // Show a message
                }

                Alarm.fromPickerResult(hours, mins, days)
                        .set(getActivity());

                adapter.notifyDataSetChanged();
            }
        });

        builder.create().show();
    }

    private Alarm.DaysOfWeek getSelectedDays(LinearLayout daysLayout) {
        int children = daysLayout.getChildCount(); // == 7, 0 is for Monday, 6 is for Sunday
        Alarm.DaysOfWeek days = new Alarm.DaysOfWeek();

        for (int i = 0; i < children; i++) {
            if (((CheckBox) daysLayout.getChildAt(i)).isChecked())
                days.set(i, true);
        }

        return days;
    }
}
