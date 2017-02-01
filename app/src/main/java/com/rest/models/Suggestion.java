package com.rest.models;

import android.support.annotation.NonNull;

import com.rest.state.Settings;

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
    public static final String NOTIFY = "notify";
    public static final int MAX_CYCLES = 10;

    // Optional null-value means that there will
    // be no notification for resting time
    private Date restTime;

    private final Date alarmTime;
    private final int cycles;
    private final int sleepHours;
    private final int sleepMins;

    public Suggestion(Date restTime, Date alarmTime, int cycles, int sleepHours, int sleepMins) {
        this.restTime = restTime;
        this.alarmTime = alarmTime;
        this.cycles = cycles;
        this.sleepHours = sleepHours;
        this.sleepMins = sleepMins;
    }

    public Suggestion(int sleepMins, int sleepHours, int cycles, Date alarmTime) {
        this.sleepMins = sleepMins;
        this.sleepHours = sleepHours;
        this.cycles = cycles;
        this.alarmTime = alarmTime;
    }


    public String getFormattedTime() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(alarmTime);
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

    public Date getRestTime() {
        return restTime;
    }

    public Date getAlarmTime() {
        return alarmTime;
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

    public boolean restNotification() {
        return restTime != null;
    }
}
