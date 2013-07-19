package org.es.api;

import android.content.Context;

import org.es.butler.pojo.Agenda;
import org.es.butler.pojo.AgendaEvent;

import java.util.List;

/**
 * Created by Cyril Leroux on 18/06/13.
 */
public interface AgendaApi {

    /**
     * @param context The application context.
     * @return The list of today's events.
     */
    List<AgendaEvent> checkTodayEvents(Context context);

    /**
     * @param context The application context.
     * @return The list of upcoming events.
     */
    List<AgendaEvent> checkUpcomingEvent(Context context);

    /**
     * @param context The application context.
     * @return The list of agendas on the device.
     */
    List<Agenda> getAgendas(Context context);
}
