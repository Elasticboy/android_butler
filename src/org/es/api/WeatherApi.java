package org.es.api;

import org.es.api.pojo.WeatherData;

/**
 * Created by Cyril Leroux on 18/06/13.
 */
public interface WeatherApi {

    WeatherData checkWeather();
    WeatherData doCheckWeather();
}
