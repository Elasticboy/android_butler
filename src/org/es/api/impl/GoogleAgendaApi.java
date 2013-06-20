package org.es.api.impl;

import org.es.api.AgendaApi;
import org.es.butler.pojo.AgendaEvent;

import java.util.List;

/**
 * Created by Cyril Leroux on 18/06/13.
 */
public class GoogleAgendaApi implements AgendaApi {

    @Override
    public List<AgendaEvent> checkTodayEvents() {
        // TODO look for today's events.
        return null;
    }

    @Override
    public List<AgendaEvent> checkUpcomingEvent() {
        // TODO look for upcoming events.
        return null;
    }
}
