package com.rest.models;

import android.support.annotation.NonNull;

import com.rest.state.Preferences;
import com.rest.util.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    private static final int NAP_DURATION = 180;
    private static final int OPTIMAL_DIFF = 1;

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

    public int totalMins() {
        return sleepHours * 60 + sleepMins;
    }

    public Date getRestAt() {
        return restAt;
    }

    public Date getAlarmAt() {
        return alarmAt;
    }

    public static List<Suggestion> reorder(List<Suggestion> suggestions) {
        // Reorders the list so the first suggestions are the best
        // And the following are sorted by cycles

        // optimals contains the optimal suggestions first
        List<Suggestion> optimals = new ArrayList<>(suggestions.size());
        List<Suggestion> nonOptimals = new ArrayList<>();

        for (Suggestion suggestion : suggestions) {
            if (suggestion.isOptimal()) {
                optimals.add(suggestion);
            } else {
                nonOptimals.add(suggestion);
            }
        }

        //Reverse the optimals so the longer times are first
        Collections.reverse(optimals);

        //Collecting all into one sorted list
        optimals.addAll(nonOptimals);
        return optimals;
    }

    private int optimality() {
        return Math.abs(cycles - Preferences.OPTIMAL_CYCLES);
    }

    // Notifies unless the scheduled rest time is less than 5 minutes away
    public boolean notifyToRest() {
        int interval = TimeUtils.intervalInMinutes(getRestAt(), TimeUtils.now());
        return getRestAt().after(TimeUtils.now()) &&
                interval > Preferences.MIN_REST_DELAY;
    }

    public boolean isNap() {
        return totalMins() < NAP_DURATION;
    }

    public boolean isOptimal() {
        return optimality() <= Suggestion.OPTIMAL_DIFF;
    }

    @Override
    public int compareTo(@NonNull Suggestion another) {
        return cycles - another.cycles;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "cycles=" + cycles +
                ", sleepHours=" + sleepHours +
                ", sleepMins=" + sleepMins +
                '}';
    }
}
