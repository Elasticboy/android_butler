package org.es.butler;

import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.es.api.AgendaApi;
import org.es.api.factory.AgendaApiFactory;
import org.es.api.pojo.Agenda;
import org.es.butler.component.AgendaAdapter;

import java.util.List;

/**
 * Created by Cyril on 20/07/13.
 */
public class AgendaList extends ListActivity {

    private static final String TAG = "AgendaList";
    private AgendaAdapter mAdapter = null;

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

        mAdapter = new AgendaAdapter(getApplicationContext(), agendas);
        setListAdapter(mAdapter);
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                SparseBooleanArray map = getListView().getCheckedItemPositions();
                StringBuilder sb = new StringBuilder();

                List<Agenda> selectedAgendas = mAdapter.getSelectedAgendas();
                for (int i = 0; i < map.size(); i++) {
                    if (map.get(i)) {
                        sb.append(i + " ");
                    }
                }
                Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (l.getCheckedItemPositions().get((int)id)) {
            v.setBackgroundColor(Color.BLUE);
        } else {
            v.setBackgroundColor(Color.LTGRAY);
        }
        super.onListItemClick(l, v, position, id);


    }
}
