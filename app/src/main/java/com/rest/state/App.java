package com.rest.state;

import android.app.AlarmManager;
import android.app.Application;
import android.util.Log;

/**
 * Created on 08/01/2017
 */
public class App extends Application {
    private static final String APP = App.class.getSimpleName();
    private static AlarmManager alarmManager;
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

        Log.d(APP, "Starting up");
        pController = new PersistenceController(this);
        pController.getSavedState();
    }


}
