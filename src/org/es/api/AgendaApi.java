package org.es.api;

import org.es.butler.pojo.AgendaEvent;

import java.util.List;

/**
 * Created by Cyril Leroux on 18/06/13.
 */
public interface AgendaApi {

    List<AgendaEvent> checkTodayEvents();
    List<AgendaEvent> checkUpcomingEvent();
}
