package com.rest.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import com.rest.R;
import com.rest.models.Suggestion;

import java.util.List;

/**
 * Created on 24/01/2017
 */
public class SuggestionAdapter extends BaseAdapter {
    private List<Suggestion> suggestionList;
    private int resID;
    private Context context;

    public SuggestionAdapter(Context context, int resource, List<Suggestion> suggestionList) {
        this.context = context;
        this.suggestionList = suggestionList;
        this.resID = resource;
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
            root = LayoutInflater.from(context).inflate(resID, null);
        }

        Suggestion suggestion = getItem(position);
        ((TextView) root.findViewById(R.id.suggestedAlarmTime))
                .setText(suggestion.getFormattedTime());
        ((TextView) root.findViewById(R.id.amountSleep))
                .setText(String.format("%dh %dm of sleep",
                        suggestion.getSleepHours(),
                        suggestion.getSleepMins()));
        ((TextView) root.findViewById(R.id.cycleCount))
                .setText(String.format("%d cycles",
                        suggestion.getCycles()));

        return root;
    }
}
