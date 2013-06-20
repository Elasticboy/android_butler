package org.es.butler.logic;

import android.content.Context;
import android.text.format.Time;

import org.es.butler.R;

/**
 * Created by Cyril Leroux on 18/06/13.
 * <p/>
 * This class defines the interpretation logic of temporal data.
 */
public class TimeLogic {

    private Time mTime;
    private Time mMorning;
    private Time mMidday;
    private Time mEvening;
    private Time mNight;

    public TimeLogic(final Time time) {
        mTime = time;
        mMorning = new Time(time);
        mMidday = new Time(time);
        mEvening = new Time(time);
        mNight = new Time(time);

        mMorning.set(0, 0, 5, time.monthDay, time.month, time.year);
        mMidday.set(0, 0, 12, time.monthDay, time.month, time.year);
        mEvening.set(0, 0, 18, time.monthDay, time.month, time.year);
        mNight.set(0, 0, 23, time.monthDay, time.month, time.year);
    }

    public Time getTime() {
        return mTime;
    }

    /**
     * @return True if time is between 05:00 -> 11:59
     */
    public boolean isMorning() {
        return mTime.equals(mMorning) || mTime.after(mMorning) && mTime.before(mMidday);
    }

    /**
     * @return True if time is between 12:00 -> 17:59
     */
    public boolean isAfternoon() {
        return mTime.equals(mMidday) || mTime.after(mMidday) && mTime.before(mEvening);
    }

    /**
     * @return True if time is between 18:00 -> 22:59
     */
    public boolean isEvening() {
        return mTime.equals(mEvening) || mTime.after(mEvening) && mTime.before(mNight);
    }

    /**
     * @return True if time is between 0:00 -> 04:59 or 23:00 -> 0:00
     */
    public boolean isNight() {
        return mTime.before(mMorning) || mTime.equals(mNight) || mTime.after(mNight);
    }

    public String getPronunciation(Context context) {
        return getPronunciationEn(mTime, context);
    }

    private String getPronunciationEn(final Time time, Context context) {
        final int minute  = time.minute;
        int hour = get12hFormat(time.hour);
        int nextHour = get12hFormat(getNextHour(time.hour));

        final StringBuilder sb = new StringBuilder();

        sb.append(context.getString(R.string.time_prefix)).append(" ");
        sb.append(get12hFormat(time.hour)).append(" ");

        if (time.minute > 0 && time.minute < 10) {
            sb.append("O ").append(time.minute);
        } else if (time.minute >= 10) {
            sb.append(time.minute);
        }

        sb.append(getSuffix(time.hour));

        return sb.toString();
//        if (minute == 0) {
//            sb.append(hour).append(" o'clock");
//
//        } else if (minute == 15) {
//            sb.append("a quarter past ").append(hour);
//
//        } else if (minute == 30) {
//            sb.append("half past ").append(hour);
//
//        } else if (minute == 45) {
//            sb.append("a quarter to ").append(nextHour);
//
//        } else if (minute < 30) {
//            sb.append(minute).append(" past ").append(hour);
//
//        } else if (minute > 30) {
//            sb.append(minute).append(" to ").append(nextHour);
//        }
    }

    private int getNextHour(int hour) {
        hour++;
        if (hour > 23) {
            return 0;
        }
        return hour;
    }

    private int get12hFormat(int hour24hFormat) {
        if (hour24hFormat == 0) {
            return 12;
        } else if (hour24hFormat > 12) {
            return hour24hFormat - 12;
        }

        return hour24hFormat;
    }

    private String getSuffix(int hour24hFormat) {
        return (hour24hFormat < 12) ? " A M." : " P M.";
    }
}
