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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import com.rest.R;
import com.rest.adapters.RepeatedAlarmAdapter;
import com.rest.models.Alarm;
import com.rest.notification.NotificationMaster;
import com.rest.state.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 06/02/2017
 */

public class RepeatingAlarmsFragment extends Fragment {
    public static final String REPTD_ALARMS_FRAGMENT =
            RepeatingAlarmsFragment.class.getSimpleName();

    private RepeatedAlarmAdapter adapter;
    private NotificationMaster notificationMaster;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationMaster = new NotificationMaster(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_repeated_alarms, container, false);

        List<Alarm> currentAlarms = App.getState().getAlarms();
        adapter = new RepeatedAlarmAdapter(getActivity(), currentAlarms);

        Button addButton = (Button) root.findViewById(R.id.add_repeating_alarm_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm();
            }
        });


        if (currentAlarms.isEmpty()) {
            root.findViewById(R.id.no_repeating_alarms).setVisibility(View.VISIBLE);
            root.findViewById(R.id.alarmsList).setVisibility(View.GONE);
            return root;
        }


        ListView alarmsList = (ListView) root.findViewById(R.id.alarmsList);
        alarmsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDialogToRemove(position);
                return true;
            }
        });

        alarmsList.setAdapter(adapter);
        return root;
    }

    private void showDialogToRemove(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Remove repeating alarm");
        builder.setMessage("Proceed?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeAlarm(position);
            }
        });

        builder.setNegativeButton(android.R.string.no, null);
        builder.show();
    }

    private void removeAlarm(int position) {
        Alarm alarm = adapter.getItem(position);

        App.getState().removeAlarm(alarm);
        notificationMaster.cancelRepeating(alarm);

        adapter.notifyDataSetChanged();
    }

    private void addAlarm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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

                ArrayList<Integer> days = getSelectedDays(daysLayout);
                Log.d(REPTD_ALARMS_FRAGMENT, String.format("Time received: %d:%d %s",
                        hours,
                        mins,
                        days));

                if (days.isEmpty()) {
                    // No days were selected. Not okay!
                    return; // Show a message
                }

                Alarm alarm = Alarm.fromPickerResult(hours, mins, days);
                App.getState().addAlarm(alarm);
                notificationMaster.scheduleForRepeating(alarm);

                adapter.notifyDataSetChanged();
            }
        });

        builder.create().show();
    }

    private ArrayList<Integer> getSelectedDays(LinearLayout daysLayout) {
        ArrayList<Integer> days = new ArrayList<>();
        int children = daysLayout.getChildCount(); // == 7, 0 is for Monday, 6 is for Sunday

        for (int i = 0; i < children; i++) {
            if (((CheckBox) daysLayout.getChildAt(i)).isChecked())
                days.add(Alarm.DAY_MAP[i]);
        }

        return days;
    }
}
