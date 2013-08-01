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

        StringBuilder sb = new StringBuilder();
        final int size = agendaIds.size();
        for (int id = 0; id < size; id++) {
            sb.append(agendaIds.get(id));
            if (id < size - 1) {
                sb.append(SEPARATOR);
            }
        }

        editor.putString(PREF_AGENDAS, sb.toString());
        editor.commit();
    }

    public static String[] loadFromPref(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        String prefAsString = preferences.getString(PREF_AGENDAS, "");

        List<String> list = new ArrayList<>();
        if (prefAsString.isEmpty()) {
            return new String[]{};
        }
        return prefAsString.split(SEPARATOR);
    }

    public static List<String> loadAsList(Context context) {
        return Arrays.asList(loadFromPref(context));
    }
}
