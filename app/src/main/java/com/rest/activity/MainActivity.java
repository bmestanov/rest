package com.rest.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rest.R;
import com.rest.fragments.ActionsFragment;
import com.rest.fragments.RepeatingAlarmsFragment;
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
                SuggestionPickFragment suggestionPickFragment =
                        new SuggestionPickFragment();

                suggestionPickFragment.setArguments(
                        makeBundle(hour, minute, Suggestion.FIXED_ALARM));

                setFragment(suggestionPickFragment, true);
            }

            @Override
            public void onFixedRestPicked(int hour, int minute) {
                SuggestionPickFragment suggestionPickFragment =
                        new SuggestionPickFragment();

                suggestionPickFragment.setArguments(
                        makeBundle(hour, minute, Suggestion.FIXED_REST));

                setFragment(suggestionPickFragment, true);
            }

            @Override
            public void onRepeatedAlarmsPicked() {
                setFragment(new RepeatingAlarmsFragment(), true);
            }

            @Override
            public void onSettingsPicked() {
                setFragment(new SettingsFragment(), true);
            }
        };

        ActionsFragment actionsFragment = new ActionsFragment();
        actionsFragment.setListener(listener);
        setFragment(actionsFragment, false);
    }

    private void setFragment(Fragment fragment, boolean pushToStack) {
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.actions_fragment, fragment);
        if (pushToStack)
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
        App.getpController().savePreferences();
    }
}