package com.rest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RatingBar;
import android.widget.Toast;

import com.rest.R;
import com.rest.state.CycleController;

/**
 * Created on 15/02/2017
 */

public class RateActivity extends Activity {
    RatingBar ratingBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                CycleController.onReceiveRating(rating);
                Toast.makeText(RateActivity.this,
                        "rest is optimizing. Have a great day!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
