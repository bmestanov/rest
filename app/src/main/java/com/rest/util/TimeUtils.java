package com.rest.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created on 01/02/2017
 */

public class TimeUtils {
    public static class HourMinute {

        public int hour, minute;

        private HourMinute(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

    }

    public static HourMinute fromTimestamp(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return new HourMinute(c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE));
    }

    public static Date now() {
        return new Date();
    }

    public static int intervalInMinutes(Date now, Date then) {
        HourMinute nowHM = fromTimestamp(now);
        HourMinute thenHM = fromTimestamp(then);

        return Math.abs(nowHM.hour - thenHM.hour) * 60 +
                Math.abs(nowHM.minute - thenHM.minute);
    }

    public static long addMinutes(long time, int minutes) {
        return time + 1000 * 60 * minutes;
    }

    public static long subtractMinutes(long time, int minutes) {
        return time - 1000 * 60 * minutes;
    }
}
