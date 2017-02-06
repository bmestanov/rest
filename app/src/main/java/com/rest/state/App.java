package com.rest.state;

import android.app.Application;
import android.util.Log;

/**
 * Created on 08/01/2017
 */
public class App extends Application {
    private static final String APP = App.class.getSimpleName();
    static State state;
    static PersistenceController pController;

    public static PersistenceController getpController() {
        return pController;
    }

    public static State getState() {
        return state;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(APP, "Starting up");
        pController = new PersistenceController(this);
        pController.loadPreferences();
        pController.loadState();
    }
}
