package com.rest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.rest.fragments.ActionsFragment;
import com.rest.fragments.SuggestionPickFragment;
import com.rest.state.App;
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
                setSuggestionFragment(hour, minute, Suggestion.FIXED_ALARM);
            }

            @Override
            public void onFixedRestPicked(int hour, int minute) {
                setSuggestionFragment(hour, minute, Suggestion.FIXED_REST);
            }

            @Override
            public void onRepeatedAlarmsPicked() {
                //For demo purposes
                //MainActivity.this.startActivity(new Intent(MainActivity.this,RateActivity.class));
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

    private void setSuggestionFragment(int hours, int mins, int mode) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
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
        //Saving the state.. just in case
        App.getpController().saveState();
    }
}