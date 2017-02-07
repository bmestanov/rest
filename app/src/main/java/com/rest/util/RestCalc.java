package com.rest.util;

import android.util.Log;

import com.rest.models.Suggestion;
import com.rest.state.CycleController;
import com.rest.state.Preferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.rest.models.Suggestion.FIXED_ALARM;
import static com.rest.models.Suggestion.MAX_CYCLES;
import static com.rest.state.CycleController.CYCLE_LENGTH;
import static com.rest.state.Preferences.SLEEP_DELAY;

/**
 * Created on 22/01/2017
 */
public class RestCalc {
    public static final String REST_CALCULATOR = RestCalc.class.getSimpleName();

    public static List<Suggestion> calculate(Date time, int mode) {
        List<Suggestion> suggestions;

        Log.d(REST_CALCULATOR, "Date received: " + time);

        suggestions = (mode == FIXED_ALARM) ?
                forFixedAlarm(time) : forFixedRest(time);

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

            long restAt = TimeUtils
                    .subtractMinutes(alarmAt,
                            SLEEP_DELAY + cycles * CYCLE_LENGTH);

            if (now < restAt) {
                suggestions.add(new Suggestion(new Date(restAt),
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

        final long restAt = restTime.getTime(); // Time of user going to rest

        int cycles = 1, sleepMins = 0;

        for (; cycles <= MAX_CYCLES; cycles++) {
            sleepMins += CYCLE_LENGTH;

            int sleepHours = sleepMins / 60;

            long suggestedAlarmAt = TimeUtils.addMinutes(restAt,
                    SLEEP_DELAY + cycles * CYCLE_LENGTH);

            Suggestion suggestion = new Suggestion(new Date(restAt),
                    new Date(suggestedAlarmAt), cycles,
                    sleepHours, sleepMins - sleepHours * 60);
            suggestions.add(suggestion);
        }

        return suggestions;
    }

    // For a given alarm time calculates the best time to show a rest notification
    // That name though...
    public static long calculateOptimalRestNotificationTime(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);

        c.add(Calendar.MINUTE, -Preferences.OPTIMAL_CYCLES *
                CycleController.CYCLE_LENGTH - Preferences.REST_DELAY - Preferences.SLEEP_DELAY);

        return time - c.getTimeInMillis();
    }
}