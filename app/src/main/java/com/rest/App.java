package com.rest;

import android.app.Application;
import android.util.Log;

/**
 * Created on 08/01/2017
 */
public class App extends Application {
    private static State state;
    private static PersistenceController pController;

    public static PersistenceController getpController() {
        return pController;
    }

    public static State getState() {
        return state;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        pController = new PersistenceController(this);
        State initialState = pController.getSavedState();

        if (initialState == null) {
            initialState = State.Default.build();
        }

        Log.d(getClass().getSimpleName(), "Initial state built");
        state = initialState;
        state.removePast();
    }
}
