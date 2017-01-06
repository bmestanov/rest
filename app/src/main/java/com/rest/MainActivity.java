package com.rest;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(null);
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
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Received time", picker.getHour() + ": " + picker.getMinute());
                    }
                });

                builder.create().show();
            }
        }


        return true;
    }
}