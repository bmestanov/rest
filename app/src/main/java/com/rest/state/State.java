package com.rest.state;

import com.rest.models.Alarm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 08/01/2017
 */

public class State {
    public static final String STATE = State.class.getSimpleName();
    private List<Alarm> activeAlarms;

    State() {
        activeAlarms = new ArrayList<>();
    }

    public void addAlarm(Alarm alarm) {
        activeAlarms.add(alarm);
    }

    public void cancelAlarm(Alarm alarm) {
        activeAlarms.remove(alarm);
    }

    public List<Alarm> getAlarms() {
        return activeAlarms;
    }
}
