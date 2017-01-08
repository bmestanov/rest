package com.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by Bilal on 08/I/2017
 */

public class PersistenceController {
    public static final String DATA = "state";
    private final SharedPreferences mPreferences;
    private final Gson mGson;

    public PersistenceController(Context context) {
        mPreferences = context.getSharedPreferences(DATA, 0);
        mGson = new Gson();
    }

    public State getSavedState() {
        if (mPreferences.contains(DATA)) {
            String json = mPreferences.getString(DATA, "");
            Log.d(getClass().getSimpleName(), "State received: " + json);
            return mGson.fromJson(json, State.class);
        }
        return null;
    }

    public void saveState() {
        String json = mGson.toJson(App.getState());
        mPreferences.edit().putString(DATA, json).apply();
        Log.d(getClass().getSimpleName(), "State saved: " + json);
    }
}
