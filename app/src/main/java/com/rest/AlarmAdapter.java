package com.rest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created on 08/01/2017
 */

public class AlarmAdapter extends ArrayAdapter<Alarm> {
    private List<Alarm> alarms;

    public AlarmAdapter(Context context, int resource, List<Alarm> alarms) {
        super(context, resource, alarms);
        this.alarms = alarms;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.alarm, null);
        }

        TextView textClock = (TextView) view.findViewById(R.id.alarmTime);
        textClock.setText(alarms.get(position).toString());
        return view;
    }
}
