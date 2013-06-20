package org.es.butler;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.es.api.AgendaApi;
import org.es.api.WeatherApi;
import org.es.api.factory.AgendaApiFactory;
import org.es.api.factory.WeatherApiFactory;
import org.es.butler.logic.TimeLogic;

import java.util.Locale;

/**
 * Created by Cyril Leroux on 17/06/13.
 */
public class MainActivity extends Activity implements OnInitListener, OnClickListener {

    private static final String TAG = "ButlerActivity";
    private TextToSpeech mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTTS = new TextToSpeech(getApplicationContext(), this);

        ((Button) findViewById(R.id.btn_daily_speech)).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
            mTTS = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.SUCCESS) {
            Log.e(TAG, "TTS initialization failed");
            return;
        }

        int result = TextToSpeech.LANG_MISSING_DATA;
        if (mTTS.isLanguageAvailable(Locale.UK) == TextToSpeech.LANG_AVAILABLE) {
            result = mTTS.setLanguage(Locale.UK);
        } else {
            result = mTTS.setLanguage(Locale.US);
        }

        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e(TAG, "This Language is not supported");
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_daily_speech:
                dailySpeech(true);
                break;

            default:
                break;
        }
    }

    /**
     * Say the daily speech
     *
     * @param force Forces the speech even if the {@link #cancelDailySpeech()} trigger returns true.
     */
    private void dailySpeech(boolean force) {
        if (cancelDailySpeech() && !force) {
            return;
        }

        WeatherApi weather = WeatherApiFactory.getWeatherAPI();
        weather.checkWeather();

        AgendaApi agenda = AgendaApiFactory.getAgendaApi();
        agenda.checkTodayEvents();
        agenda.checkUpcomingEvent();

        Time now = new Time();
        now.setToNow();

        TimeLogic time = new TimeLogic(now);
        sayHello(time);
        sayTime(time);

        // TODO : Change hard coded sentences with real values !
        mTTS.speak("The temperature is 24 degrees Celsius.", TextToSpeech.QUEUE_ADD, null);
        mTTS.speak("It's a bit rainy today. Don't forget to cover yourself.", TextToSpeech.QUEUE_ADD, null);
        mTTS.speak("1 event found in your calendar.", TextToSpeech.QUEUE_ADD, null);
        mTTS.speak("Your Jujitsu course is at 8 30 pm.", TextToSpeech.QUEUE_ADD, null);
        //mTTS.speak("And you have no appointment today.", TextToSpeech.QUEUE_ADD, null);
    }

    private boolean cancelDailySpeech() {
        // TODO implement the conditions to cancel daily speech
        return false;
    }

    /**
     * Say hello out loud.
     * The spoken text depends on the time of day.
     *
     * @param time The time defining the spoken text.
     */
    private void sayHello(final TimeLogic time) {

        String text = null;

        if (time.isMorning()) {
            text = getString(R.string.good_morning);

        } else if (time.isAfternoon()) {
            text = getString(R.string.good_afternoon);

        } else if (time.isEvening()) {
            text = getString(R.string.good_evening);

        } else if (time.isNight()) {
            text = getString(R.string.good_night);

        } else {
           Log.e(TAG, "Unexpected Time value : " + time.getTime().format("HH:mm:ss"));
        }

        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    /**
     * Say the time out loud.
     *
     * @param time the time to speak out loud.
     */
    private void sayTime(final TimeLogic time) {
        mTTS.setSpeechRate(0.9f);
        final String text = time.getPronunciation(getApplicationContext());
        if (text == null || text.isEmpty()) {
            Log.e(TAG, "sayTime(), couldn't get pronunciation.");
        }

        mTTS.speak(text, TextToSpeech.QUEUE_ADD, null);

        mTTS.setSpeechRate(1f);
    }
}
