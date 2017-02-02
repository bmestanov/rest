package com.rest.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;

import com.rest.models.Suggestion;
import com.rest.util.TimeUtils;

/**
 * Created on 01/02/2017
 */

public class NotificationMaster {
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
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //Intent to notify user to rest
        if (suggestion.notifyToRest()) {
            Intent restNotificationIntent = new Intent(context, AlarmReceiver.class);
            restNotificationIntent.putExtra(NOTIFICATION_TYPE, EVENT_REST_TIME);

            PendingIntent pendingRestIntent = PendingIntent
                    .getBroadcast(context, EVENT_REST_TIME, restNotificationIntent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    suggestion.getNotifyAt().getTime(),
                    pendingRestIntent);

            Log.d(getClass().getSimpleName(), "Rest notification set to " + suggestion.getNotifyAt());
        }

        //Intent to fire when the alarm occurs
        Intent alarmNotificationIntent = new Intent(context, AlarmReceiver.class);
        alarmNotificationIntent.putExtra(NOTIFICATION_TYPE, EVENT_ALARM);
        PendingIntent pendingAlarmIntent = PendingIntent
                .getBroadcast(context, EVENT_ALARM, alarmNotificationIntent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP,
                suggestion.getAlarmAt().getTime(),
                pendingAlarmIntent);

        Log.d(getClass().getSimpleName(), "Alarm notification set to " + suggestion.getAlarmAt());

        //Intent to set the alarm itself
        Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);

        TimeUtils.HourMinute hm = TimeUtils.fromTimestamp(suggestion.getAlarmAt());
        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, hm.hour);
        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, hm.minute);
        alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        context.startActivity(alarmIntent);

        Log.d(getClass().getSimpleName(), "Alarm set to " + suggestion.getAlarmAt());
    }
}
