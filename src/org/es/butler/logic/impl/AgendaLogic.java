package org.es.butler.logic.impl;

import android.content.Context;

import org.es.butler.R;
import org.es.butler.logic.PronunciationLogic;
import org.es.butler.pojo.AgendaEvent;
import org.es.butler.pojo.WeatherData;

import java.util.List;

/**
 * Created by Cyril Leroux on 24/06/13.
 */
public class AgendaLogic implements PronunciationLogic {

    List<AgendaEvent> mEvents;

    public AgendaLogic(List<AgendaEvent> events) {
        mEvents = events;
    }

    @Override
    public String getPronunciation(Context context) {
        return getPronunciationEn(mEvents, context);
    }

    private String getPronunciationEn(final List<AgendaEvent> events, Context context) {
        int count = (events == null || events.isEmpty()) ? 0 : events.size();
        return context.getString(R.plurals.appointment_count) + " Your Jujitsu course is at 8 30 pm.";
        //return "You have no appointment today.";
    }
}
