package org.es.api.impl;

import android.text.format.Time;

import org.es.api.WeatherApi;
import org.es.butler.pojo.WeatherData;

/**
 * Created by Cyril on 02/07/13.
 */
public abstract class AbstractWeatherApi implements WeatherApi {

    private static final long UPDATE_TIME_MILLIS = 10 * 60 * 1000; // 10 minutes

    protected WeatherData mSavedData;
    protected long mLastUpdate;

    protected boolean isUpdateRequired() {
        if (mSavedData == null) {
            return true;
        }

        Time now = new Time();
        now.setToNow();

        if (mLastUpdate + UPDATE_TIME_MILLIS < now.toMillis(false)) {
            return true;
        }
        return false;
    }

    public WeatherData checkWeather() {
       return isUpdateRequired() ? doCheckWeather() : mSavedData;
    }

}
