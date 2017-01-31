package com.rest.util;

import com.rest.models.Suggestion;
import com.rest.state.Settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created on 22/01/2017
 */
public class RestCalc {
    public static List<Suggestion> calculate(Date datetime, int mode) {
        List<Suggestion> suggestions = new ArrayList<>();

        long now = System.currentTimeMillis();
        long then = datetime.getTime();

        int cycles = 0, sleepMins = 0;
        if (mode == Suggestion.FIXED_ALARM) {
            while (now < then) {
                cycles++;
                sleepMins += Settings.CYCLE_LENGTH;

                int sleepHours = sleepMins / 60;

                then -= 1000 * 60 * Settings.CYCLE_LENGTH;
                suggestions.add(new Suggestion(new Date(then),
                        cycles,
                        sleepHours,
                        sleepMins - sleepHours * 60));
            }
        }


        Collections.sort(suggestions); //Optimal times first
        return suggestions;
    }
}