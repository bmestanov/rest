package com.rest.state;

import com.rest.models.Alarm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 08/01/2017
 */

public class State {
    public static transient final String STATE = State.class.getSimpleName();
    private List<Alarm> activeAlarms;

    State() {
        activeAlarms = new ArrayList<>();
    }

    public void addAlarm(Alarm alarm) {
        activeAlarms.add(alarm);
        App.pController.saveState();
    }

    public void removeAlarm(Alarm alarm) {
        activeAlarms.remove(alarm);
        App.pController.saveState();
    }

    public List<Alarm> getAlarms() {
        return activeAlarms;
    }

    public int getAlarmID() {
        int id = (int) (Math.random() * 10_000);

        for (Alarm alarm : activeAlarms) {
            if (alarm.getId() == id)
                return getAlarmID();
        }

        return id;
    }
}
