package com.rest.state;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.rest.state.Settings.Default;
import com.rest.state.Settings.Key;

/**
 * Created by Bilal on 08/I/2017
 */

public class PersistenceController {
    public static final String PERSISTENCE_CTRL = PersistenceController.class.getSimpleName();
    public static final String SHARED_PREFS = "shared_prefs";
    private final SharedPreferences mPreferences;

    public PersistenceController(Context context) {
        mPreferences = context.getSharedPreferences(SHARED_PREFS, 0);
    }

    public void getSavedState() {
        Settings.CYCLE_LENGTH = mPreferences.getInt(Key.CYCLE_LENGTH_KEY,
                Default.CYCLE_LENGTH);
        Settings.REST_DELAY = mPreferences.getInt(Key.REST_DELAY_KEY,
                Default.REST_DELAY);
        Settings.SLEEP_DELAY = mPreferences.getInt(Key.SLEEP_DELAY_KEY,
                Default.SLEEP_DELAY);

        Log.d(PERSISTENCE_CTRL, "Cycle length received: " + Settings.CYCLE_LENGTH);
        Log.d(PERSISTENCE_CTRL, "Rest delay received: " + Settings.REST_DELAY);
        Log.d(PERSISTENCE_CTRL, "Sleep delay received: " + Settings.SLEEP_DELAY);
    }

    public void saveState() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(Key.CYCLE_LENGTH_KEY, Settings.CYCLE_LENGTH);
        editor.putInt(Key.REST_DELAY_KEY, Settings.REST_DELAY);
        editor.putInt(Key.SLEEP_DELAY_KEY, Settings.SLEEP_DELAY);

        editor.apply();

        Log.d(PERSISTENCE_CTRL, "Cycle length saved: " + Settings.CYCLE_LENGTH);
        Log.d(PERSISTENCE_CTRL, "Rest delay saved: " + Settings.REST_DELAY);
        Log.d(PERSISTENCE_CTRL, "Sleep delay saved: " + Settings.SLEEP_DELAY);
    }
}
