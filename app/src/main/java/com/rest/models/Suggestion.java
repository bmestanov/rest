package com.rest.models;

import android.support.annotation.NonNull;

import com.rest.state.Settings;
import com.rest.util.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created on 24/01/2017
 */
public class Suggestion implements Comparable<Suggestion> {

    public static final int FIXED_ALARM = 3;
    public static final int FIXED_REST = 4;

    public static final String MINUTE = "minute";
    public static final String HOUR = "hour";
    public static final String MODE = "mode";
    public static final int MAX_CYCLES = 10;

    private final Date notifyAt;
    private final Date alarmAt;
    private final int cycles;
    private final int sleepHours;
    private final int sleepMins;

    public Suggestion(Date notifyAt, Date alarmAt, int cycles, int sleepHours, int sleepMins) {
        this.notifyAt = notifyAt;
        this.alarmAt = alarmAt;
        this.cycles = cycles;
        this.sleepHours = sleepHours;
        this.sleepMins = sleepMins;
    }


    public String getFormattedTime(int time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        if (time == Suggestion.FIXED_ALARM) {
            return simpleDateFormat.format(notifyAt);
        } else {
            return simpleDateFormat.format(alarmAt);
        }

    }

    public int getSleepMins() {
        return sleepMins;
    }

    public int getSleepHours() {
        return sleepHours;
    }

    public int getCycles() {
        return cycles;
    }

    private int totalMins() {
        return sleepHours * 60 + sleepMins;
    }

    public Date getNotifyAt() {
        return notifyAt;
    }

    public Date getAlarmAt() {
        return alarmAt;
    }

    @Override
    public int compareTo(@NonNull Suggestion other) {
        int opt1 = optimality();
        int opt2 = other.optimality();
        if (opt1 == opt2) {
            return cycles - other.cycles;
        } else {
            return optimality() - other.optimality();
        }
    }

    private int optimality() {
        return Math.abs(totalMins() - Settings.OPTIMAL_SLEEP);
    }

    public boolean notifyToRest() {
        return TimeUtils.intervalInMinutes(notifyAt, TimeUtils.now()) > Settings.REST_DELAY;
    }
}
