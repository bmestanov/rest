package com.rest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Bilal on 08/I/2017
 */

public class Alarm {
    private transient AlarmManager alarmManager;
    private long dateTime;
    private List<Integer> days;
    private boolean active;

    public Alarm(long dateTime, List<Integer> days, Context context) {
        this.dateTime = dateTime;
        this.days = days;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void set(Context context) {
        //Add this alarm to the app state
        App.getState().addAlarm(this);

        //Add a pending intent to the alarmManager
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC, dateTime, pendingIntent);
    }

    public void cancel(Context context) {
        //Remove this alarm from the app state
        App.getState().cancelAlarm(this);

        //Remove the pending intent
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.cancel(sender);
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

    @Override
    public String toString() {
        return SimpleDateFormat.getTimeInstance().format(new Date(dateTime));
    }
}
