package com.rest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rest.R;
import com.rest.fragments.ActionsFragment;
import com.rest.fragments.SettingsFragment;
import com.rest.fragments.SuggestionPickFragment;
import com.rest.models.Suggestion;
import com.rest.state.App;

import static com.rest.fragments.ActionsFragment.OnActionSelectedListener;

public class MainActivity extends AppCompatActivity {
    public static final String MAIN = MainActivity.class.getSimpleName();
    private OnActionSelectedListener listener;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listener = new OnActionSelectedListener() {
            @Override
            public void onFixedAlarmPicked(int hour, int minute) {
                setSuggestionFragment(hour, minute, Suggestion.FIXED_ALARM);
            }

            @Override
            public void onFixedRestPicked(int hour, int minute) {
                setSuggestionFragment(hour, minute, Suggestion.FIXED_REST);
            }

            @Override
            public void onRepeatedAlarmsPicked() {
                //For demo purposes
                MainActivity.this.startActivity(new Intent(MainActivity.this, RateActivity.class));
            }

            @Override
            public void onSettingsPicked() {
                setSettingsFragment();
            }
        };

        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ActionsFragment actionsFragment = new ActionsFragment();
        actionsFragment.setListener(listener);

        ft.add(R.id.actions_fragment, actionsFragment);
        ft.commit();
    }

    private void setSettingsFragment() {
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        SettingsFragment settingsFragment =
                new SettingsFragment();

        ft.replace(R.id.actions_fragment, settingsFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void setSuggestionFragment(int hours, int mins, int mode) {
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        SuggestionPickFragment suggestionPickFragment =
                new SuggestionPickFragment();

        suggestionPickFragment.setArguments(
                makeBundle(hours, mins, mode));

        ft.replace(R.id.actions_fragment, suggestionPickFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private Bundle makeBundle(int hours, int mins, int mode) {
        Bundle bundle = new Bundle(3);
        bundle.putInt(Suggestion.HOUR, hours);
        bundle.putInt(Suggestion.MINUTE, mins);
        bundle.putInt(Suggestion.MODE, mode);
        return bundle;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(MAIN, "Shutting down");
        //Saving the state.. just in case
        App.getpController().saveSettings();
    }
}