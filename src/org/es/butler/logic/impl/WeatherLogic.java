package org.es.butler.logic.impl;

import android.content.Context;

import org.es.butler.logic.PronunciationLogic;
import org.es.api.pojo.WeatherData;

/**
 * Created by Cyril Leroux on 20/06/13.
 * <p/>
 * This class defines the interpretation logic of weather data.
 */
public class WeatherLogic implements PronunciationLogic {

    private WeatherData mWeatherData;

    public WeatherLogic(WeatherData data) {
        mWeatherData = data;
    }

    @Override
    public String getPronunciation(Context context) {
        return getPronunciationEn(mWeatherData, context);
    }

    private String getPronunciationEn(final WeatherData weather, Context context) {
       // // TODO : Change hard coded sentences with real values !
       //return "The temperature is 24 degrees Celsius. " +
       //"It's a bit rainy today. Don't forget to cover yourself.";
        return "";
    }
}
