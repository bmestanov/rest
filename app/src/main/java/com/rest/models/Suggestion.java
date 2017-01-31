package com.rest.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created on 24/01/2017
 */
public class Suggestion implements Comparable<Suggestion> {
    public static final int BED_TIME = 0;
    public static final int ALARM_TIME = 1;


    public static final String MINUTE = "minute";
    public static final String HOUR = "hour";
    public static final int FIXED_ALARM = 3;
    public static final String MODE = "mode";
    private static final int OPTIMAL_SLEEP = 480;

    private final Date time;
    private final int cycles;
    private final int sleepHours;
    private final int sleepMins;

    public Suggestion(Date time, int cycles, int sleepHours, int sleepMins) {
        this.time = time;
        this.cycles = cycles;
        this.sleepHours = sleepHours;
        this.sleepMins = sleepMins;
    }


    public String getFormattedTime() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(time);
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


    @Override
    public int compareTo(Suggestion another) {
        return Math.abs(totalMins() - Suggestion.OPTIMAL_SLEEP) -
                Math.abs(another.totalMins() - Suggestion.OPTIMAL_SLEEP);
    }
}
