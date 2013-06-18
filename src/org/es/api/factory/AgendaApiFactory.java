package org.es.api.factory;

import org.es.api.AgendaApi;
import org.es.api.impl.GoogleAgendaApi;

/**
 * Created by Cyril Leroux on 18/06/13.
 */
public class AgendaApiFactory {

    public static synchronized AgendaApi getAgendaApi() {
        return new GoogleAgendaApi();
    }
}
