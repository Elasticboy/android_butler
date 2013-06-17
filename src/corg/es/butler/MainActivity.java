package corg.es.butler;

import java.util.Locale;

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

/**
 * 
 * @author Cyril Leroux
 *
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

	}

	private boolean cancelDailySpeech() {
		// TODO implement
		return false;
	}

	private void sayHello() {
		Time now	= new Time();
		now.setToNow();

		Time fouram		= new Time(now);
		fouram.set(0, 0, 4, now.monthDay, now.month, now.year);
		Time noon		= new Time(now);
		noon.set(0, 0, 12, now.monthDay, now.month, now.year);
		Time sixpm		= new Time(now);
		sixpm.set(0, 0, 18, now.monthDay, now.month, now.year);
		Time elevenpm	= new Time(now);
		elevenpm.set(0, 0, 23, now.monthDay, now.month, now.year);

		String text = null;
		//04:00 -> 11:59
		if (now.after(fouram) && now.before(noon)) {
			text = getString(R.string.good_morning);
		}
		// 12:00 -> 17:59
		else if (now.after(noon) || now.before(sixpm)) {
			text = getString(R.string.good_afternoon);
		}
		// 18:00 -> 22:59
		else if (now.after(sixpm) || now.before(elevenpm)) {
			text = getString(R.string.good_evening);

		} else { // 0:00 -> 03:59 or 23:00 -> 0:00
			text = getString(R.string.good_night);
		}

		mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);

	}
}
