package com.rest.fragments;

import android.app.Fragment;
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
import com.rest.util.TimeUtils;

import java.util.Date;
import java.util.List;

/**
 * Created on 24/01/2017
 */

public class SuggestionPickFragment extends Fragment {
    private Date datetime;
    private int mode;


    public SuggestionPickFragment() {
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        int hours = args.getInt(Suggestion.HOUR);
        int mins = args.getInt(Suggestion.MINUTE);

        datetime = TimeUtils.fromHourMinute(hours, mins);
        mode = args.getInt(Suggestion.MODE);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_suggestions, container, false);
        List<Suggestion> suggestions = RestCalc.calculate(datetime, mode);

        ListView listView = (ListView) root.findViewById(R.id.suggestions_list);

        if (suggestions.isEmpty()) { // Show no suggestion TextView
            listView.setVisibility(View.GONE);
            root.findViewById(R.id.no_repeating_alarms).setVisibility(View.VISIBLE);
            return root;
        }

        final SuggestionAdapter suggestionAdapter = new SuggestionAdapter(getActivity(),
                suggestions, mode);
        listView.setAdapter(suggestionAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new NotificationMaster(getActivity(), suggestionAdapter.getItem(position))
                        .scheduleNotifications();
                getActivity().onBackPressed();
                //getActivity().finish(); // We're done with the app
            }
        });
        return root;
    }


}
