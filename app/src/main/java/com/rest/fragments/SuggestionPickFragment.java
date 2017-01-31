package com.rest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rest.R;
import com.rest.adapters.SuggestionAdapter;
import com.rest.models.Suggestion;
import com.rest.util.RestCalc;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created on 24/01/2017
 */

public class SuggestionPickFragment extends android.support.v4.app.Fragment {
    private Date datetime;
    private static int MODE;


    public SuggestionPickFragment() {
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        int hours = args.getInt(Suggestion.HOUR);
        int mins = args.getInt(Suggestion.MINUTE);

        Calendar calendar = Calendar.getInstance(); //A calendar set to now
        //ToDo Test this
        int now = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, mins);

        if (now > hours) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        datetime = new Date(calendar.getTimeInMillis());
        MODE = args.getInt(Suggestion.MODE);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.suggestion_fragment, container, false);
        List<Suggestion> suggestions = RestCalc.calculate(datetime, MODE);

        ListView listView = (ListView) root.findViewById(R.id.suggestions_list);
        SuggestionAdapter suggestionAdapter = new SuggestionAdapter(getContext(),
                R.layout.alarm_suggestion,
                suggestions);
        listView.setAdapter(suggestionAdapter);
        return root;
    }


}
