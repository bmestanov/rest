package com.rest.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rest.R;
import com.rest.models.Suggestion;

import java.util.List;

/**
 * Created on 24/01/2017
 */
public class SuggestionAdapter extends BaseAdapter {
    private static final int RES_ID = R.layout.alarm_suggestion;

    private List<Suggestion> suggestionList;
    private Context context;
    private int mode;

    public SuggestionAdapter(Context context, List<Suggestion> suggestionList, int mode) {
        this.context = context;
        this.suggestionList = suggestionList;
        this.mode = mode;
    }

    @Override
    public int getCount() {
        return suggestionList.size();
    }

    @Nullable
    @Override
    public Suggestion getItem(int position) {
        return suggestionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root = convertView;

        if (root == null) {
            root = LayoutInflater.from(context).inflate(RES_ID, null);
        }

        Suggestion suggestion = getItem(position);

        if (suggestion.isOptimal()) {
            root.findViewById(R.id.suggestion_layout)
                    .setBackgroundColor(ContextCompat.getColor(context,
                            android.R.color.background_dark));
        } else {
            root.findViewById(R.id.suggestion_layout)
                    .setBackgroundColor(ContextCompat.getColor(context,
                            R.color.colorPrimaryDarker));
        }

        ((TextView) root.findViewById(R.id.suggestedAlarmTime))
                .setText(suggestion.getFormattedTime(mode));
        ((TextView) root.findViewById(R.id.amountSleep))
                .setText(String.format("%dh %dm of %s",
                        suggestion.getSleepHours(),
                        suggestion.getSleepMins(),
                        suggestion.isNap() ? "nap" : "sleep"));

        int cycles = suggestion.getCycles();
        ((TextView) root.findViewById(R.id.cycleCount))
                .setText(cycles == 1 ? cycles + " cycle" : cycles + " cycles");
        return root;
    }
}
