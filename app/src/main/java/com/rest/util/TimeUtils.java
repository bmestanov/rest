package com.rest.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created on 01/02/2017
 */

public class TimeUtils {
    public static final String HH_MM_FORMAT = "HH:mm";

    public static Date fromHourMinute(int hours, int mins) {
        Calendar calendar = Calendar.getInstance(); //A calendar set to now

        int nowHours = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMins = calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, mins);

        if (nowHours > hours ||
                (nowHours == hours && nowMins > mins)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return new Date(calendar.getTimeInMillis());
    }

    public static HourMinute fromDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return new HourMinute(c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE));
    }

    public static Date now() {
        return new Date();
    }

    public static int intervalInMinutes(Date now, Date then) {
        HourMinute nowHM = fromDate(now);
        HourMinute thenHM = fromDate(then);

        return Math.abs(nowHM.hour - thenHM.hour) * 60 +
                Math.abs(nowHM.minute - thenHM.minute);
    }

    public static long addMinutes(long time, int minutes) {
        return time + 1000 * 60 * minutes;
    }

    public static long subtractMinutes(long time, int minutes) {
        return time - 1000 * 60 * minutes;
    }

    public static String format(String pattern, Date time) {
        return new SimpleDateFormat(pattern, Locale.getDefault())
                .format(time);
    }

    public static String format(String pattern, long time) {
        return new SimpleDateFormat(pattern, Locale.getDefault())
                .format(new Date(time));
    }

    public static class HourMinute {

        public int hour, minute;

        public HourMinute(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

    }
}
