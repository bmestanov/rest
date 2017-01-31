package com.rest;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.rest.fragments.ActionsFragment;
import com.rest.fragments.SuggestionPickFragment;
import com.rest.models.App;
import com.rest.models.Suggestion;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ActionsFragment actionsFragment = new ActionsFragment();
        actionsFragment.setListener(new ActionsFragment.OnTimePickedListener() {
            @Override
            public void onPick(int hour, int minute) {
                setSuggestionFragment(hour, minute);
            }
        });

        ft.add(R.id.actions_fragment, actionsFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStack();
    }


    private void setSuggestionFragment(int hours, int mins) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SuggestionPickFragment suggestionPickFragment =
                new SuggestionPickFragment();

        suggestionPickFragment.setArguments(getBundle(hours, mins, Suggestion.FIXED_ALARM));

        //ft.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
        ft.replace(R.id.actions_fragment, suggestionPickFragment);
        ft.addToBackStack(SuggestionPickFragment.class.getSimpleName());
        // Start the animated transition.
        ft.commit();
    }

    private Bundle getBundle(int hours, int mins, int mode) {
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