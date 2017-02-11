package com.rest.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;

import com.rest.models.Alarm;
import com.rest.models.Suggestion;
import com.rest.state.Preferences;
import com.rest.util.RestCalc;
import com.rest.util.TimeUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created on 01/02/2017
 */

public class NotificationMaster {
    public static final String NOTIFICATION_MASTER = NotificationMaster.class.getSimpleName();
    public static final String NOTIFICATION_TYPE = "notification_type";
    public static final int EVENT_REST = 1;
    public static final int EVENT_FEEDBACK = 2;
    private Context context;
    private AlarmManager alarmManager;
    private Suggestion suggestion;

    public NotificationMaster(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }


    public void scheduleForSingleEvent(Suggestion suggestion) {
        this.suggestion = suggestion;

        //Intent to notify user to rest
        if (suggestion.notifyToRest()) {
            scheduleSingleRestNotification();
        } else {
            Log.d(NOTIFICATION_MASTER, "No rest notification set. Time received: "
                    + suggestion.getRestAt());
        }

        //Intent to fire when the alarm occurs
        scheduleSingleFeedbackNotification();

        //Intent to set the alarm itself
        setAlarm(suggestion.getAlarmAt().getTime(), null);
    }

    public void scheduleForRepeating(Alarm alarm) {
        long optimalNotifyToRestTime = RestCalc
                .calculateOptimalRestNotificationTime(alarm.getTime());

        Intent restIntent = new Intent(context, NotificationReceiver.class);
        restIntent.putExtra(NOTIFICATION_TYPE, EVENT_REST);

        Intent feedbackIntent = new Intent(context, NotificationReceiver.class);
        feedbackIntent.putExtra(NOTIFICATION_TYPE, EVENT_FEEDBACK);

        PendingIntent restSender = PendingIntent.getBroadcast(context,
                alarm.getId(), restIntent, 0);
        PendingIntent feedbackSender = PendingIntent.getBroadcast(context,
                alarm.getId(), feedbackIntent, 0);

        for (Long alarmTime : alarm.listTimestamps()) {
            long notifyAt = alarmTime - optimalNotifyToRestTime;
            //Set a notification PendingIntent
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    notifyAt, AlarmManager.INTERVAL_DAY * 7, restSender);

            Log.d(NOTIFICATION_MASTER, "Set rest notification for " + new Date(notifyAt));

            //Set a feedback PendingIntent
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    alarmTime, AlarmManager.INTERVAL_DAY * 7, feedbackSender);

            Log.d(NOTIFICATION_MASTER, "Set feedback notification for " + new Date(alarmTime));

        }

        setAlarm(alarm.getTime(), alarm.getDays());
    }

    public void cancelRepeating(Alarm alarm) {
        Intent alarmNotificationIntent = new Intent(context, NotificationReceiver.class);

        PendingIntent sender = PendingIntent
                .getBroadcast(context, alarm.getId(), alarmNotificationIntent, 0);

        //Cancels all alarms with the same ID
        alarmManager.cancel(sender);

        Log.d(NOTIFICATION_MASTER, "Cancelled alarm " + alarm);
    }

    private void scheduleSingleRestNotification() {
        Intent restNotificationIntent = new Intent(context, NotificationReceiver.class);
        restNotificationIntent.putExtra(NOTIFICATION_TYPE, EVENT_REST);

        long notificationTime = TimeUtils.subtractMinutes(suggestion.getRestAt().getTime(),
                Preferences.REST_DELAY);
        PendingIntent sender = PendingIntent
                .getBroadcast(context, EVENT_REST, restNotificationIntent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                notificationTime,
                sender);

        Log.d(NOTIFICATION_MASTER, "Rest notification set to "
                + new Date(notificationTime));
    }

    private void scheduleSingleFeedbackNotification() {
        Intent feedbackIntent = new Intent(context, NotificationReceiver.class);
        feedbackIntent.putExtra(NOTIFICATION_TYPE, EVENT_FEEDBACK);

        PendingIntent feedbackSender = PendingIntent
                .getBroadcast(context, EVENT_FEEDBACK, feedbackIntent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                suggestion.getAlarmAt().getTime(),
                feedbackSender);

        Log.d(NOTIFICATION_MASTER, "Alarm notification set to " + suggestion.getAlarmAt());
    }

    private void setAlarm(long alarmTime, ArrayList<Integer> days) {
        Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);

        TimeUtils.HourMinute hm = TimeUtils.fromDate(alarmTime);
        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, hm.hour);
        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, hm.minute);
        alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        if (days != null) {
            alarmIntent.putExtra(AlarmClock.EXTRA_DAYS, days);
        }
        context.startActivity(alarmIntent);

        Log.d(NOTIFICATION_MASTER, "Alarm set to " + new Date(alarmTime));
    }
}
