package com.rest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rest.R;
import com.rest.models.Alarm;
import com.rest.util.TimeUtils;

import java.util.List;

/**
 * Created on 06/02/2017
 */

public class RepeatedAlarmAdapter extends BaseAdapter {
    public static final int RES_ID = R.layout.alarm;

    private List<Alarm> alarms;
    private Context context;

    public RepeatedAlarmAdapter(Context context, List<Alarm> alarms) {
        this.context = context;
        this.alarms = alarms;
    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public Alarm getItem(int position) {
        return alarms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root = convertView;

        if (root == null) {
            root = LayoutInflater.from(context).inflate(RES_ID, null);
        }

        Alarm alarm = getItem(position);

        ((TextView) root.findViewById(R.id.alarmTime))
                .setText(TimeUtils.format(TimeUtils.HH_MM_FORMAT, alarm.getTime()));
        ((TextView) root.findViewById(R.id.days))
                .setText(alarm.getDays(context));

        return root;
    }
}
