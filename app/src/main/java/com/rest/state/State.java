package com.rest.state;

import com.rest.models.Alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created on 08/01/2017
 */

public class State {
    private List<Alarm> activeAlarms;
    private Settings settings;

    private State(List<Alarm> activeAlarms, Settings settings) {
        this.activeAlarms = activeAlarms;
        this.settings = settings;
    }

    public void addAlarm(Alarm alarm) {
        activeAlarms.add(alarm);
        App.getpController().saveSettings();
        //MainActivity.alarmsAdapter.notifyDataSetChanged();
    }

    public void cancelAlarm(Alarm alarm) {
        activeAlarms.remove(alarm);
        App.getpController().saveSettings();
        //MainActivity.alarmsAdapter.notifyDataSetChanged();
    }

    public void removePast() {
        //Deactivates all alarms that refer to the past
        long now = Calendar.getInstance().getTimeInMillis();
        List<Alarm> toRemove = new ArrayList<>(activeAlarms.size());
        for (int i = 0; i < activeAlarms.size(); i++) {
            if (activeAlarms.get(i).getTime() < now)
                toRemove.add(activeAlarms.get(i));
        }

        activeAlarms.removeAll(toRemove);
    }

    public List<Alarm> getAlarms() {
        return activeAlarms;
    }

    public static class Default {
        public static State build() {
            return new State(new ArrayList<Alarm>(),
                    new Settings());
        }
    }
}
