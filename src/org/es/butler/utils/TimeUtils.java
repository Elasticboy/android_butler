package org.es.butler.utils;

import android.text.format.Time;

import corg.es.butler.R;

/**
 * Created by Cyril Leroux on 18/06/13.
 */
public class TimeUtils {

    /**
     *
     * @param time
     * @return True if time is between 05:00 -> 11:59
     */
    public static boolean isMorning(final Time time) {
        Time fiveAm = new Time(time);
        Time midday = new Time(time);
        fiveAm.set(0, 0, 5, time.monthDay, time.month, time.year);
        midday.set(0, 0, 12, time.monthDay, time.month, time.year);

        return time.after(fiveAm) && time.before(midday);
    }

    /**
     *
     * @param time
     * @return True if time is between 12:00 -> 17:59
     */
    public static boolean isAfternoon(final Time time) {
        Time midday = new Time(time);
        Time sixPm = new Time(time);
        midday.set(0, 0, 12, time.monthDay, time.month, time.year);
        sixPm.set(0, 0, 18, time.monthDay, time.month, time.year);

        return time.after(midday) && time.before(sixPm);
    }

    /**
     *
     * @param time
     * @return True if time is between 18:00 -> 22:59
     */
    public static boolean isEvening(final Time time) {
        Time sixPm = new Time(time);
        Time elevenPm = new Time(time);
        sixPm.set(0, 0, 18, time.monthDay, time.month, time.year);
        elevenPm.set(0, 0, 23, time.monthDay, time.month, time.year);

        return time.after(sixPm) && time.before(elevenPm);
    }

    /**
     *
     * @param time
     * @return True if time is between 0:00 -> 03:59 or 23:00 -> 0:00
     */
    public static boolean isNight(final Time time) {
        Time fourAm = new Time(time);
        fourAm.set(0, 0, 4, time.monthDay, time.month, time.year);
        Time elevenPm = new Time(time);
        elevenPm.set(0, 0, 23, time.monthDay, time.month, time.year);

        return time.before(fourAm) || time.after(elevenPm);
    }
}
