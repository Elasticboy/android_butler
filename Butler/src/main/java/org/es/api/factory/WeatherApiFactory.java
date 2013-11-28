package org.es.api.factory;

import org.es.api.WeatherApi;
import org.es.api.impl.WundergroundWeatherApi;

/**
 * Created by Cyril Leroux on 18/06/13.
 */
public class WeatherApiFactory {

    public static synchronized WeatherApi getWeatherAPI() {
        return new WundergroundWeatherApi();
    }
}
