package org.es.api.dao;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Cyril on 24/07/13.
 */
public class AgendaDao {

    private static final String SHARED_PREF     = "org.es.butler";
    private static final String PREF_AGENDAS    = "agendas_pref";
    private static final String SEPARATOR       = ";";

    public static void saveToPref(Context context, List<String> agendaIds) {

        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        for (int i = 0; i < agendaIds.size(); i++) {
            editor.putString(PREF_AGENDAS, agendaIds.get(i) + SEPARATOR);
        }

        editor.commit();
    }

    public static List<String> loadFromPref(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        String prefAsString = preferences.getString(PREF_AGENDAS, "");

        return Arrays.asList(prefAsString.split(SEPARATOR));
    }
}
