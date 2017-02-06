package com.rest.state;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.rest.state.Preferences.Default;
import com.rest.state.Preferences.Key;

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

    public void loadPreferences() {
        //Also initialized cycle length as it'll be used in calculations
        CycleController.CYCLE_LENGTH = mPreferences.getInt(
                CycleController.Constants.CYCLE_LENGTH_KEY,
                Default.CYCLE_LENGTH);

        Preferences.REST_DELAY = mPreferences.getInt(Key.REST_DELAY_KEY,
                Default.REST_DELAY);
        Preferences.SLEEP_DELAY = mPreferences.getInt(Key.SLEEP_DELAY_KEY,
                Default.SLEEP_DELAY);

        Log.d(PERSISTENCE_CTRL, "Cycle length received: " + CycleController.CYCLE_LENGTH);
        Log.d(PERSISTENCE_CTRL, "Rest delay received: " + Preferences.REST_DELAY);
        Log.d(PERSISTENCE_CTRL, "Sleep delay received: " + Preferences.SLEEP_DELAY);
    }

    public void savePreferences() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(Key.REST_DELAY_KEY, Preferences.REST_DELAY);
        editor.putInt(Key.SLEEP_DELAY_KEY, Preferences.SLEEP_DELAY);

        editor.apply();

        Log.d(PERSISTENCE_CTRL, "Rest delay saved: " + Preferences.REST_DELAY);
        Log.d(PERSISTENCE_CTRL, "Sleep delay saved: " + Preferences.SLEEP_DELAY);
    }

    public void loadCycleVariables() {
        CycleController.ALIGN_DIRECTION = mPreferences.getInt(
                CycleController.Constants.ALIGN_DIRECTION_KEY,
                CycleController.Constants.DEFAULT_ALIGN_DIRECTION);

        CycleController.CONFIDENCE = mPreferences.getInt(
                CycleController.Constants.CONFIDENCE_KEY,
                CycleController.Constants.DEFAULT_CONFIDENCE);

        Log.d(PERSISTENCE_CTRL, "CycleController Align direction received: " +
                CycleController.ALIGN_DIRECTION);
        Log.d(PERSISTENCE_CTRL, "CycleController Confidence received: " +
                CycleController.CONFIDENCE);
    }

    public void saveCycleVariables() {
        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putInt(CycleController.Constants.ALIGN_DIRECTION_KEY,
                CycleController.ALIGN_DIRECTION);
        editor.putInt(CycleController.Constants.CONFIDENCE_KEY,
                CycleController.CONFIDENCE);
        editor.putInt(CycleController.Constants.CYCLE_LENGTH_KEY,
                CycleController.CYCLE_LENGTH);

        editor.apply();
    }

    public void loadState() {
        String savedState = mPreferences.getString(State.STATE, null);

        App.state = new Gson().fromJson(savedState, State.class);

        if (App.state == null) {
            App.state = new State();
        }
        Log.d(PERSISTENCE_CTRL, "State loaded: " + savedState);
    }

    public void saveState() {
        String currentState = new Gson().toJson(App.state);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(State.STATE, currentState);
        editor.apply();

        Log.d(PERSISTENCE_CTRL, "State saved: " + currentState);
    }
}
