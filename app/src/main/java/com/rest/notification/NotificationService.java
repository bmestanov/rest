package com.rest.notification;

/**
 * Created by Bilal on 6.1.2017 г..
 */

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.rest.R;
import com.rest.activity.RateActivity;
import com.rest.models.Alarm;
import com.rest.state.App;
import com.rest.state.Preferences;

public class NotificationService extends IntentService {
    public NotificationService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "In the intent service!");

        String action = intent.getAction();


        PendingIntent contentIntent = null;

        //Boot is completed - so reschedule all repeating alarms
        if (rescheduleOnBoot(action)) {
            return;
        }

        String msg;
        if (action.equals(NotificationMaster.EVENT_FEEDBACK) && Preferences.FEEDBACK_ON) {
            contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, RateActivity.class), 0);
            msg = "How do you feel?";
        } else {
            msg = "Time for a good nap.";
        }

        sendNotification(contentIntent, msg);

        Log.d("AlarmService", "Notification sent.");
    }

    private boolean rescheduleOnBoot(String action) {
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            NotificationMaster nMaster = new NotificationMaster(getBaseContext());
            for (Alarm alarm : App.getState().getAlarms()) {
                nMaster.scheduleForRepeating(alarm);
            }
            return true;
        }
        return false;
    }

    private void sendNotification(PendingIntent contentIntent, String msg) {
        NotificationManager alarmNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        NotificationCompat.Builder alarmNotificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Hey there!")
                .setSmallIcon(R.drawable.ic_notification)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setAutoCancel(true)
                .setContentText(msg);

        if (contentIntent != null) {
            alarmNotificationBuilder.setContentIntent(contentIntent);
        }

        alarmNotificationManager.notify(1, alarmNotificationBuilder.build());
    }
}
