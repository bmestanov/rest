package com.rest;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.rest.fragments.ActionsFragment;
import com.rest.fragments.SuggestionPickFragment;
import com.rest.models.App;
import com.rest.models.Suggestion;

import static com.rest.fragments.ActionsFragment.OnActionSelectedListener;

public class MainActivity extends AppCompatActivity {

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
                setSuggestionFragment(hour, minute, Suggestion.FIXED_ALARM, true);
            }

            @Override
            public void onFixedRestPicked(int hour, int minute, boolean notify) {
                setSuggestionFragment(hour, minute, Suggestion.FIXED_REST, notify);
            }

            @Override
            public void onRepeatedAlarmsPicked() {

            }

            @Override
            public void onSettingsPicked() {

            }
        };

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ActionsFragment actionsFragment = new ActionsFragment();
        actionsFragment.setListener(listener);

        ft.add(R.id.actions_fragment, actionsFragment);
        ft.commit();
    }

    private void setSuggestionFragment(int hours, int mins, int mode, boolean notify) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SuggestionPickFragment suggestionPickFragment =
                new SuggestionPickFragment();

        suggestionPickFragment.setArguments(
                makeBundle(hours, mins, mode, notify));

        ft.replace(R.id.actions_fragment, suggestionPickFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private Bundle makeBundle(int hours, int mins, int mode, boolean notify) {
        Bundle bundle = new Bundle(3);
        bundle.putInt(Suggestion.HOUR, hours);
        bundle.putInt(Suggestion.MINUTE, mins);
        bundle.putBoolean(Suggestion.NOTIFY, notify);
        bundle.putInt(Suggestion.MODE, mode);
        return bundle;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Saving the state.. just in case
        App.getpController().saveState();
    }
}