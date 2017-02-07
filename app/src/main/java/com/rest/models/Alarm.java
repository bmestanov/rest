package com.rest.models;

import android.annotation.SuppressLint;
import android.content.Context;

import com.rest.R;
import com.rest.state.App;
import com.rest.util.TimeUtils;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Bilal on 08/I/2017
 */

public final class Alarm {
    public static final String ALARM_EXTRA = "ALARM_EXTRA";
    private static transient int EVERY_DAY = 7;

    public static int[] DAY_MAP = new int[]{
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY,
            Calendar.SUNDAY,
    };
    int id;
    private boolean notify;
    private long dateTime;
    private ArrayList<Integer> days;

    public Alarm() {
    }

    public static Alarm fromPickerResult(int hour, int minute, ArrayList<Integer> days) {
        Alarm alarm = new Alarm();

        alarm.id = App.getState().getAlarmID();
        alarm.notify = true;
        alarm.days = days;
        alarm.dateTime = TimeUtils.fromHourMinute(hour, minute).getTime();
        return alarm;
    }


    public long getTime() {
        return dateTime;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public String toString() {
        return TimeUtils.format(TimeUtils.HH_MM_FORMAT, dateTime);
    }

    public int getId() {
        return id;
    }

    public List<Long> listTimestamps() {
        List<Long> times = new ArrayList<>(7);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dateTime);

        for (Integer day : days) {
            c.set(Calendar.DAY_OF_WEEK, day);
            times.add(c.getTimeInMillis());
        }

        return times;
    }

    public ArrayList<Integer> getDays() {
        return days;
    }

    public String getDaysFormatted(Context context) {
        StringBuilder sBuilder = new StringBuilder();

        if (days.isEmpty()) {
            sBuilder.append(context.getString(R.string.never));
        } else if (days.size() == EVERY_DAY) {
            sBuilder.append(context.getString(R.string.every_day));
        } else {
            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] dayList = (days.size() > 1) ?
                    dfs.getShortWeekdays() :
                    dfs.getWeekdays();

            for (Integer day : days) {
                sBuilder.append(dayList[day]);
                sBuilder.append(" ");
            }
        }

        return sBuilder.toString();
    }
}

