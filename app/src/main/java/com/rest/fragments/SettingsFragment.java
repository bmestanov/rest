package com.rest.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.rest.R;
import com.rest.state.Settings;

/**
 * Created on 04/02/2017
 */

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @SuppressLint("DefaultLocale")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_fragment);

        //Setting up current values
        Preference cycleLength = findPreference(Settings.Key.CYCLE_LENGTH_KEY);
        cycleLength.setSummary(getString(R.string.cycle_length_summary) +
                String.format(" %d minutes.", Settings.CYCLE_LENGTH));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Settings.Key.REST_DELAY_KEY)) {
            Settings.REST_DELAY = Integer.valueOf(sharedPreferences
                    .getString(key, ""));
        } else if (key.equals(Settings.Key.SLEEP_DELAY_KEY)) {
            Settings.SLEEP_DELAY = Integer.valueOf(sharedPreferences
                    .getString(key, ""));
        }
    }
}
