package com.rest.util;

import com.rest.models.Suggestion;
import com.rest.state.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created on 22/01/2017
 */
public class RestCalc {
    public static List<Suggestion> calculate(Date datetime, int mode, boolean notify) {
        List<Suggestion> suggestions = new ArrayList<>();

        if (mode == Suggestion.FIXED_ALARM) {
            forFixedAlarm(datetime, suggestions);
        } else if (mode == Suggestion.FIXED_REST) {
            forFixedRest(datetime, notify, suggestions);
        }

        Collections.sort(suggestions); //Optimal times first
        return suggestions;
    }

    private static void forFixedAlarm(Date datetime, List<Suggestion> suggestions) {
        long now = System.currentTimeMillis();
        long then = datetime.getTime();

        int cycles = 0, sleepMins = 0;

        now += 1000 * 60 * (Settings.REST_DELAY + Settings.SLEEP_DELAY);

        while (now < then) {
            cycles++;
            sleepMins += Settings.CYCLE_LENGTH;

            int sleepHours = sleepMins / 60;

            then = TimeUtils.subtractMinutes(then, Settings.CYCLE_LENGTH);
            if (now < then) {
                suggestions.add(new Suggestion(new Date(now),
                        new Date(then),
                        cycles,
                        sleepHours,
                        sleepMins - sleepHours * 60));
            }
        }
    }

    private static void forFixedRest(Date datetime, boolean notify, List<Suggestion> suggestions) {
        long then = datetime.getTime();
        final long sleepTime = TimeUtils.subtractMinutes(then, Settings.REST_DELAY);

        int cycles = 1, sleepMins = 0;

        then = TimeUtils.addMinutes(then, Settings.SLEEP_DELAY);
        for (; cycles <= Suggestion.MAX_CYCLES; cycles++) {
            sleepMins += Settings.CYCLE_LENGTH;

            int sleepHours = sleepMins / 60;

            then = TimeUtils.addMinutes(then, Settings.CYCLE_LENGTH);
            Suggestion suggestion = new Suggestion(notify ? new Date(sleepTime) : null,
                    new Date(then), cycles,
                    sleepHours, sleepMins - sleepHours * 60);
            suggestions.add(suggestion);
        }
    }
}