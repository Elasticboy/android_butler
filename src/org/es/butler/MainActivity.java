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
import org.es.butler.utils.TimeUtils;

import java.util.Locale;

import corg.es.butler.R;

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
		}
		int result = mTTS.setLanguage(Locale.US);

		if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
			Log.e(TAG, "This Language is not supported");
		}
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()) {
		case R.id.btn_daily_speech :
			dailySpeech(true);
			break;

		default:
			break;
		}
	}

	/**
	 * Say the daily speech
	 * @param force Forces the speech even if the {@link #cancelDailySpeech()} trigger returns true.
	 */
	private void dailySpeech(boolean force) {
		if (cancelDailySpeech() && !force) {
			return;
		}

		sayHello();

        WeatherApi weather = WeatherApiFactory.getWeatherAPI();
        weather.checkWeather();

        AgendaApi agenda = AgendaApiFactory.getAgendaApi();
        agenda.checkTodayEvents();
        agenda.checkUpcomingEvent();

	}

	private boolean cancelDailySpeech() {
		// TODO implement the conditions to cancel daily speech
		return false;
	}

    /**
     * Say hello out loud.
     * The spoken text depends on the time of day.
     */
	private void sayHello() {
		Time now	= new Time();
		now.setToNow();

		String text = null;

		if (TimeUtils.isMorning(now)) {
			text = getString(R.string.good_morning);

		} else if (TimeUtils.isAfternoon(now)) {
			text = getString(R.string.good_afternoon);

		} else if (TimeUtils.isEvening(now)) {
			text = getString(R.string.good_evening);

		} else if (TimeUtils.isNight(now)) {
			text = getString(R.string.good_night);
		}

		mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
}
