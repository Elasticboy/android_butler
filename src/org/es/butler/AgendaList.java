package org.es.butler;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

import org.es.api.AgendaApi;
import org.es.api.dao.AgendaDao;
import org.es.api.factory.AgendaApiFactory;
import org.es.api.pojo.Agenda;
import org.es.butler.component.AgendaAdapter;
import org.es.butler.utils.IntentKey;

import java.util.ArrayList;
import java.util.Arrays;
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

        String[] selectedNames = getIntent().getStringArrayExtra(IntentKey.AGENDA_LIST_INTENT);
        List<String> selected = (selectedNames != null) ? Arrays.asList(selectedNames) : new ArrayList<String>();
        mAdapter = new AgendaAdapter(getApplicationContext(), agendas, selected);
        setListAdapter(mAdapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:

                returnSelectedAgendas();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void returnSelectedAgendas() {
        checkList();

        ArrayList<String> returnedList = new ArrayList<>();

        // For each element in the status array
        final SparseBooleanArray selectedAgendas = mAdapter.getSelectedAgendas();
        final int agendaCount = selectedAgendas.size();
        for (int i = 0; i < agendaCount; ++i) {
            // This tells us the item position we are looking at
            final int itemPosition = selectedAgendas.keyAt(i);

            // And this tells us the item status at the above position
            final boolean isChecked = selectedAgendas.valueAt(i);

            // And we can get our data from the adapter like that
            final Agenda agenda = (Agenda) mAdapter.getItem(itemPosition);

            if (isChecked) {
                returnedList.add(agenda.getDisplayName());
            }
        }

        Intent returnIntent = new Intent();
        returnIntent.putStringArrayListExtra(IntentKey.AGENDA_LIST_INTENT, returnedList);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    protected void checkList() {
        StringBuilder sb = new StringBuilder();

        sb.append("native implementation : ");
        final SparseBooleanArray checkedItems = getListView().getCheckedItemPositions();
        // For each element in the status array
        final int checkedItemsCount = checkedItems.size();
        for (int i = 0; i < checkedItemsCount; ++i) {
            // This tells us the item position we are looking at
            final int itemPosition = checkedItems.keyAt(i);

            // And this tells us the item status at the above position
            final boolean isChecked = checkedItems.valueAt(i);

            // And we can get our data from the adapter like that
            final Agenda currentItem = (Agenda) mAdapter.getItem(itemPosition);
            sb.append(itemPosition).append(":").append(isChecked).append(" ");
        }
        sb.append("\n");

        sb.append("owned implementation : ");
        // For each element in the status array
        final SparseBooleanArray selectedAgendas = mAdapter.getSelectedAgendas();
        final int agendaCount = selectedAgendas.size();
        for (int i = 0; i < agendaCount; ++i) {
            // This tells us the item position we are looking at
            final int itemPosition = selectedAgendas.keyAt(i);

            // And this tells us the item status at the above position
            final boolean isChecked = selectedAgendas.valueAt(i);

            // And we can get our data from the adapter like that
            final Agenda currentItem = (Agenda) mAdapter.getItem(itemPosition);
            sb.append(itemPosition).append(":").append(isChecked).append(" ");
        }

        Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
    }
}
