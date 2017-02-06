package com.rest.models;

import android.support.annotation.NonNull;

import com.rest.state.Preferences;
import com.rest.util.TimeUtils;

import java.util.Date;

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

    private final Date restAt;
    private final Date alarmAt;
    private final int cycles;
    private final int sleepHours;
    private final int sleepMins;

    public Suggestion(Date restAt, Date alarmAt, int cycles, int sleepHours, int sleepMins) {
        this.restAt = restAt;
        this.alarmAt = alarmAt;
        this.cycles = cycles;
        this.sleepHours = sleepHours;
        this.sleepMins = sleepMins;
    }


    public String getFormattedTime(int mode) {
        return (mode == FIXED_ALARM) ?
                TimeUtils.format(TimeUtils.HH_MM_FORMAT, restAt) :
                TimeUtils.format(TimeUtils.HH_MM_FORMAT, alarmAt);
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

    public Date getRestAt() {
        return restAt;
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
        return Math.abs(totalMins() - Preferences.OPTIMAL_SLEEP);
    }

    public boolean notifyToRest() {
        int interval = TimeUtils.intervalInMinutes(getRestAt(), TimeUtils.now());
        return getRestAt().after(TimeUtils.now()) &&
                interval > Preferences.MIN_REST_DELAY;
    }
}
