package org.es.api.impl;

import android.text.format.Time;

import org.es.api.WeatherApi;
import org.es.butler.pojo.WeatherData;

/**
 * Created by Cyril Leroux on 18/06/13.
 */
public class WundergroundWeatherApi extends AbstractWeatherApi {

    @Override
    public WeatherData doCheckWeather() {
        // TODO get prevision for the day
        return null;
    }
}
