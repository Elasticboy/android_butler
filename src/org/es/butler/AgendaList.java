package org.es.butler;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.es.api.AgendaApi;
import org.es.api.factory.AgendaApiFactory;
import org.es.butler.pojo.Agenda;

import java.util.List;

/**
 * Created by Cyril on 20/07/13.
 */
public class AgendaList extends ListActivity {

    private static final String TAG = "AgendaList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_list);
    }

    @Override
    protected void onStart() {
        super.onStart();

        AgendaApi agendaApi = AgendaApiFactory.getAgendaApi();
        List<Agenda> agendas = agendaApi.getAgendas(getApplicationContext());

        String[] names = new String[agendas.size()];
        int index = 0;
        for (Agenda agenda : agendas) {
            names[index++] = agenda.getDisplayName() + "\n" + agenda.getOwnerName();
            Log.d(TAG, agenda.getId() + " | " + agenda.getAccountName() + " | " + agenda.getDisplayName() + " | " + agenda.getOwnerName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                names);

        getListView().setAdapter(adapter);
    }
}
