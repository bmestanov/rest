package com.rest.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.rest.R;
import com.rest.state.CycleController;
import com.rest.state.Preferences;

/**
 * Created on 04/02/2017
 */

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @SuppressLint("DefaultLocale")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);

        //Setting up current values
        setSleepLength();

        //Set up the resetting option
        findPreference(Preferences.RESET_CYCLE_LENGTH)
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        confirmReset();
                        return true;
                    }
                });
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
        if (key.equals(Preferences.Key.REST_DELAY_KEY)) {
            Preferences.REST_DELAY = Integer.valueOf(sharedPreferences
                    .getString(key, ""));
        } else if (key.equals(Preferences.Key.SLEEP_DELAY_KEY)) {
            Preferences.SLEEP_DELAY = Integer.valueOf(sharedPreferences
                    .getString(key, ""));
        } else if (key.equals(Preferences.Key.FEEDBACK_ON_KEY)) {
            Preferences.FEEDBACK_ON = Boolean.valueOf(sharedPreferences
                    .getString(key, "true"));
        }
    }

    @SuppressLint("DefaultLocale")
    private void setSleepLength() {
        Preference cycleLength = findPreference(CycleController.Constants.CYCLE_LENGTH_KEY);
        cycleLength.setSummary(getString(R.string.cycle_length_summary) +
                String.format(" %d minutes.", CycleController.CYCLE_LENGTH));
    }

    private void confirmReset() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getString(R.string.reset_cycle_length));
        builder.setMessage(getActivity().getString(R.string.warn_reset_dialog));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CycleController.reset();
                //Update the UI
                setSleepLength();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }


}
