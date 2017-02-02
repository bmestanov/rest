package com.rest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rest.R;
import com.rest.adapters.SuggestionAdapter;
import com.rest.models.Suggestion;
import com.rest.notification.NotificationMaster;
import com.rest.util.RestCalc;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created on 24/01/2017
 */

public class SuggestionPickFragment extends android.support.v4.app.Fragment {
    private Date datetime;
    private int mode;


    public SuggestionPickFragment() {
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        int hours = args.getInt(Suggestion.HOUR);
        int mins = args.getInt(Suggestion.MINUTE);

        Calendar calendar = Calendar.getInstance(); //A calendar set to now
        //ToDo Test this
        int nowHours = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMins = calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, mins);

        if (nowHours > hours || (nowHours == hours && nowMins > mins)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        datetime = new Date(calendar.getTimeInMillis());
        mode = args.getInt(Suggestion.MODE);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.suggestion_fragment, container, false);
        List<Suggestion> suggestions = RestCalc.calculate(datetime, mode);

        ListView listView = (ListView) root.findViewById(R.id.suggestions_list);
        final SuggestionAdapter suggestionAdapter = new SuggestionAdapter(getContext(),
                R.layout.alarm_suggestion,
                suggestions, mode);
        listView.setAdapter(suggestionAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new NotificationMaster(getContext(), suggestionAdapter.getItem(position))
                        .scheduleNotifications();
                getActivity().onBackPressed();
            }
        });
        return root;
    }


}
