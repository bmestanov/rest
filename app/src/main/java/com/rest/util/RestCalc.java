package com.rest.util;

import com.rest.models.Suggestion;
import com.rest.state.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.rest.models.Suggestion.FIXED_ALARM;
import static com.rest.models.Suggestion.MAX_CYCLES;
import static com.rest.state.Settings.CYCLE_LENGTH;
import static com.rest.state.Settings.REST_DELAY;
import static com.rest.state.Settings.SLEEP_DELAY;

/**
 * Created on 22/01/2017
 */
public class RestCalc {
    public static List<Suggestion> calculate(Date datetime, int mode) {
        List<Suggestion> suggestions;

        suggestions = (mode == FIXED_ALARM) ?
                forFixedAlarm(datetime) : forFixedRest(datetime);

        Collections.sort(suggestions); //Optimal times first
        return suggestions;
    }

    private static List<Suggestion> forFixedAlarm(Date alarmTime) {
        List<Suggestion> suggestions = new ArrayList<>();

        final long now = System.currentTimeMillis(); // The current time
        final long alarmAt = alarmTime.getTime();        // Time of the alarm

        int cycles = 0, sleepMins = 0;

        while (true) {
            cycles++;
            sleepMins += CYCLE_LENGTH;

            int sleepHours = sleepMins / 60;

            long notifyAt = TimeUtils
                    .subtractMinutes(alarmAt,
                            REST_DELAY + SLEEP_DELAY + cycles * CYCLE_LENGTH);

            if (now < notifyAt) {
                suggestions.add(new Suggestion(new Date(notifyAt),
                        new Date(alarmAt),
                        cycles,
                        sleepHours,
                        sleepMins - sleepHours * 60));
            } else {
                break;
            }
        }

        return suggestions;
    }

    private static List<Suggestion> forFixedRest(Date restTime) {
        List<Suggestion> suggestions = new ArrayList<>(MAX_CYCLES);

        final long now = restTime.getTime(); // Time of user going to rest

        final long notifyAt = TimeUtils.subtractMinutes(now,
                REST_DELAY); // Show notification before REST_DELAY mins

        int cycles = 1, sleepMins = 0;

        for (; cycles <= MAX_CYCLES; cycles++) {
            sleepMins += CYCLE_LENGTH;

            int sleepHours = sleepMins / 60;

            long suggestedAlarmAt = TimeUtils.addMinutes(now,
                    SLEEP_DELAY + cycles * CYCLE_LENGTH);

            Suggestion suggestion = new Suggestion(new Date(notifyAt),
                    new Date(suggestedAlarmAt), cycles,
                    sleepHours, sleepMins - sleepHours * 60);
            suggestions.add(suggestion);
        }

        return suggestions;
    }
}