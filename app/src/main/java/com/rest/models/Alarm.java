package com.rest.models;

import android.annotation.SuppressLint;
import android.content.Context;

import com.rest.R;
import com.rest.state.App;
import com.rest.util.TimeUtils;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * Created by Bilal on 08/I/2017
 */

public class Alarm {
    private static final String ALARM_EXTRA = "ALARM_EXTRA";

    private long dateTime;
    private DaysOfWeek days;
    private String label;


    public Alarm() {
    }

    public static Alarm fromPickerResult(int hour, int minute, DaysOfWeek days) {
        Alarm alarm = new Alarm();

        alarm.days = days;
        alarm.dateTime = TimeUtils.fromHourMinute(hour, minute).getTime();
        return alarm;
    }

    public void set(Context context) {
        App.getState().addAlarm(this);
        App.getpController().saveState();

        //Go call the notification master here
    }

    public void cancel(Context context) {
        App.getState().cancelAlarm(this);
        App.getpController().saveState();
    }

    public long getTime() {
        return dateTime;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public String toString() {
        return TimeUtils.format(TimeUtils.HH_MM_FORMAT, dateTime);
    }

    public String getDays(Context context) {
        return days.toString(context, false);
    }

    /*
            Bitmask representation of active days
            as inspired by Google's Alarm Clock implementation

    */
    public static class DaysOfWeek {
        private static int[] DAY_MAP = new int[]{
                Calendar.MONDAY,
                Calendar.TUESDAY,
                Calendar.WEDNESDAY,
                Calendar.THURSDAY,
                Calendar.FRIDAY,
                Calendar.SATURDAY,
                Calendar.SUNDAY,
        };


        private int mDays;

        public DaysOfWeek() {
        }

        public String toString(Context context, boolean showNever) {
            StringBuilder stringBuilder = new StringBuilder();

            // no days (0000000)
            if (mDays == 0) {
                return showNever ?
                        context.getText(R.string.never).toString() : "";
            }

            // every day (1111111)
            if (mDays == 0x7f) {
                return context.getText(R.string.every_day).toString();
            }

            // count selected days
            int dayCount = 0, days = mDays;
            while (days > 0) {
                if ((days & 1) == 1) dayCount++;
                days = days >> 1;
            }

            // short or long form?
            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] dayList = (dayCount > 1) ?
                    dfs.getShortWeekdays() :
                    dfs.getWeekdays();

            // selected days
            for (int i = 0; i < 7; i++) {
                if ((mDays & (1 << i)) != 0) {
                    stringBuilder.append(dayList[DAY_MAP[i]]);
                    dayCount -= 1;
                    if (dayCount > 0) stringBuilder.append(" ");
                }
            }
            return stringBuilder.toString();
        }

        private boolean isSet(int day) {
            return ((mDays & (1 << day)) > 0);
        }

        public void set(int day, boolean set) {
            if (set) {
                mDays |= (1 << day);
            } else {
                mDays &= ~(1 << day);
            }
        }

        public void set(DaysOfWeek dow) {
            mDays = dow.mDays;
        }

        public int getCoded() {
            return mDays;
        }

        // Returns days of week encoded in an array of booleans.
        public boolean[] getBooleanArray() {
            boolean[] ret = new boolean[7];
            for (int i = 0; i < 7; i++) {
                ret[i] = isSet(i);
            }
            return ret;
        }

        public boolean isRepeatSet() {
            return mDays != 0;
        }

        /**
         * returns number of days from today until next alarm
         *
         * @param c must be set to today
         */
        public int getNextAlarm(Calendar c) {
            if (mDays == 0) {
                return -1;
            }

            int today = (c.get(Calendar.DAY_OF_WEEK) + 5) % 7;

            int day = 0;
            int dayCount = 0;
            for (; dayCount < 7; dayCount++) {
                day = (today + dayCount) % 7;
                if (isSet(day)) {
                    break;
                }
            }
            return dayCount;
        }
    }


}

