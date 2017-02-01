package com.rest.models;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.rest.notification.AlarmReceiver;
import com.rest.R;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Bilal on 08/I/2017
 */

public class Alarm implements Parcelable {
    private static final String ALARM_EXTRA = "ALARM_EXTRA";

    //////////////////////////////
    // Parcelable apis
    //////////////////////////////


    private Alarm(Parcel p) {
        this.dateTime = p.readLong();
        this.days = new DaysOfWeek(p.readInt());
        this.label = p.readString();
        this.active = p.readInt() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(dateTime);
        dest.writeInt(days.getCoded());
        dest.writeString(label);
        dest.writeInt(active ? 1 : 0);
    }

    public static final Parcelable.Creator<Alarm> CREATOR
            = new Parcelable.Creator<Alarm>() {
        public Alarm createFromParcel(Parcel p) {
            return new Alarm(p);
        }

        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    //////////////////////////////

    private long dateTime;
    private DaysOfWeek days;
    private String label;
    private boolean active;


    public Alarm(long dateTime, List<Integer> days, String label, boolean active) {
        this.dateTime = dateTime;
        this.label = label;
        this.active = active;
        this.days = new DaysOfWeek(DaysOfWeek.DAY_MAP[1]);
    }

    public void set(Context context) {
        //Add this alarm to the app state
        App.getState().addAlarm(this);

        //Add a pending intent to the alarmManager
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra(ALARM_EXTRA, this);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE))
                .set(AlarmManager.RTC, dateTime, pendingIntent);
        //MainActivity.alarmsAdapter.notifyDataSetChanged();
    }

    public void cancel(Context context) {
        //Remove this alarm from the app state
        App.getState().cancelAlarm(this);

        //Remove the pending intent
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE))
                .cancel(sender);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            intent = new Intent("android.intent.action.ALARM_CHANGED");
            intent.putExtra("alarmSet", false);
            context.sendBroadcast(intent);
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getTime() {
        return dateTime;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public String toString() {
        return new SimpleDateFormat("HH:mm").format(new Date(dateTime));
    }

    public boolean isActive() {
        return active;
    }

    /*
            Bitmask representation of active days
            as inspired by Google's Alarm Clock implementation

    */
    private static class DaysOfWeek {
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

        DaysOfWeek(int days) {
            mDays = days;
        }

        public String toString(Context context, boolean showNever) {
            StringBuilder ret = new StringBuilder();

            // no days
            if (mDays == 0) {
                return showNever ?
                        context.getText(R.string.never).toString() : "";
            }

            // every day
            if (mDays == 0x7f) {
                return context.getText(R.string.every_day).toString();
            }

            // count selected days
            int dayCount = 0, days = mDays;
            while (days > 0) {
                if ((days & 1) == 1) dayCount++;
                days >>= 1;
            }

            // short or long form?
            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] dayList = (dayCount > 1) ?
                    dfs.getShortWeekdays() :
                    dfs.getWeekdays();

            // selected days
            for (int i = 0; i < 7; i++) {
                if ((mDays & (1 << i)) != 0) {
                    ret.append(dayList[DAY_MAP[i]]);
                    dayCount -= 1;
                    if (dayCount > 0) ret.append(
                            context.getText(R.string.day_concat));
                }
            }
            return ret.toString();
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

