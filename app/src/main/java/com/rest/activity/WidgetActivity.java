package com.rest.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.rest.fragments.SuggestionPickFragment;
import com.rest.models.Suggestion;
import com.rest.util.TimeUtils;

/**
 * Created on 15/02/2017
 */

public class WidgetActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        SuggestionPickFragment fragment = new SuggestionPickFragment();

        TimeUtils.HourMinute now = TimeUtils.fromDate(System.currentTimeMillis());
        fragment.setArguments(MainActivity.makeBundle(
                now.hour, now.minute, Suggestion.FIXED_REST
        ));

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.replace(android.R.id.content, fragment);
        transaction.commit();
    }
}
