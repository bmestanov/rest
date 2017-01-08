package com.rest;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TimePicker;

import com.google.gson.Gson;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ListView alarmsList;
    ArrayAdapter<Alarm> alarmsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        alarmsList = (ListView) findViewById(R.id.alarmsList);
        alarmsAdapter = new AlarmAdapter(this, R.layout.alarm, App.getState().getAlarms());
        alarmsList.setAdapter(alarmsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_alarm: {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View root = LayoutInflater.from(this).inflate(R.layout.time_picker, null);
                builder.setView(root);
                final TimePicker picker = (TimePicker) root.findViewById(R.id.timePicker);
                picker.setIs24HourView(true);

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hours, mins;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            hours = picker.getHour();
                            mins = picker.getMinute();
                        } else {
                            hours = picker.getCurrentHour();
                            mins = picker.getCurrentMinute();
                        }

                        setAlarm(hours, mins);
                    }
                });

                builder.create().show();
            }
        }

        return true;
    }

    private void setAlarm(int hours, int mins) {
        Log.d("Received time", hours + " : " + mins);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, mins);
        new Alarm(calendar.getTimeInMillis(), null, this).set(this);
        alarmsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Saving the state.. just in case
        App.getpController().saveState();
    }
}