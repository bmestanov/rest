package com.rest.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;

import com.rest.models.Suggestion;
import com.rest.state.Preferences;
import com.rest.util.TimeUtils;

import java.util.Date;

/**
 * Created on 01/02/2017
 */

public class NotificationMaster {
    public static final String NOTIFICATION_MASTER = NotificationMaster.class.getSimpleName();
    public static final String NOTIFICATION_TYPE = "notification_type";
    public static final int EVENT_REST_TIME = 1;
    public static final int EVENT_ALARM = 2;
    private Suggestion suggestion;
    private Context context;

    public NotificationMaster(Context context, Suggestion suggestion) {
        this.suggestion = suggestion;
        this.context = context;
    }

    public void scheduleNotifications() {
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        //Intent to notify user to rest
        if (suggestion.notifyToRest()) {
            scheduleRestNotification(alarmManager);
        } else {
            Log.d(NOTIFICATION_MASTER, "No rest notification set. Time received: "
                    + suggestion.getRestAt());
        }

        //Intent to fire when the alarm occurs
        scheduleAlarmNotification(alarmManager);

        //Intent to set the alarm itself
        setAlarm();
    }

    private void setAlarm() {
        Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);

        TimeUtils.HourMinute hm = TimeUtils.fromDate(suggestion.getAlarmAt());
        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, hm.hour);
        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, hm.minute);
        alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        context.startActivity(alarmIntent);

        Log.d(NOTIFICATION_MASTER, "Alarm set to " + suggestion.getAlarmAt());
    }

    private void scheduleAlarmNotification(AlarmManager alarmManager) {
        Intent alarmNotificationIntent = new Intent(context, AlarmReceiver.class);
        alarmNotificationIntent.putExtra(NOTIFICATION_TYPE, EVENT_ALARM);
        PendingIntent pendingAlarmIntent = PendingIntent
                .getBroadcast(context, EVENT_ALARM, alarmNotificationIntent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP,
                suggestion.getAlarmAt().getTime(),
                pendingAlarmIntent);

        Log.d(NOTIFICATION_MASTER, "Alarm notification set to " + suggestion.getAlarmAt());
    }

    private void scheduleRestNotification(AlarmManager alarmManager) {
        Intent restNotificationIntent = new Intent(context, AlarmReceiver.class);
        restNotificationIntent.putExtra(NOTIFICATION_TYPE, EVENT_REST_TIME);

        long notificationTime = TimeUtils.subtractMinutes(suggestion.getRestAt().getTime(),
                Preferences.REST_DELAY);
        PendingIntent pendingRestIntent = PendingIntent
                .getBroadcast(context, EVENT_REST_TIME, restNotificationIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                notificationTime,
                pendingRestIntent);

        Log.d(NOTIFICATION_MASTER, "Rest notification set to "
                + new Date(notificationTime));
    }
}
