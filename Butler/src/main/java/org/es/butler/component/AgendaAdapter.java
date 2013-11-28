package org.es.butler.component;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.es.api.pojo.Agenda;
import org.es.butler.R;

import java.util.List;

/**
 * Created by Cyril on 21/07/13.
 */
public class AgendaAdapter extends BaseAdapter {

    private List<Agenda> mAgendas = null;
    private SparseBooleanArray mSelectedAgendas = null;
    private LayoutInflater mInflater;


    private int mSelectedTextColor = 0;
    private int mDefaultTextColor = 0;

    public AgendaAdapter(Context context, List<Agenda> agendas, List<String> selectedNames) {
        mInflater = LayoutInflater.from(context);
        mAgendas = agendas;
        mSelectedAgendas = new SparseBooleanArray(agendas.size());

        final int agendaCount = agendas.size();
        for (int i = 0; i < agendaCount; i++) {
            boolean alreadySelected = selectedNames.contains(agendas.get(i).getDisplayName());
            mSelectedAgendas.put(i, alreadySelected);
        }

        mSelectedTextColor = context.getResources().getColor(android.R.color.primary_text_light);
        mDefaultTextColor = context.getResources().getColor(android.R.color.primary_text_light_nodisable);
    }

    @Override
    public int getCount() {
        if (mAgendas == null) {
            return 0;
        }
        return mAgendas.size();
    }

    @Override
    public Object getItem(int pos) {
        if (mAgendas == null) {
            return null;
        }
        return mAgendas.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    public static class ViewHolder {
        Color color;
        long id;
        TextView displayName;
        TextView accountName;
        TextView ownerName;
        CheckBox checkBox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.agenda_item, null);
            holder = new ViewHolder();
            // Draw a color rect
            // holder.color        = (ImageView) _convertView.findViewById(R.id.ivThumbnail);
            holder.displayName = (TextView) convertView.findViewById(R.id.agenda_title);
            //holder.accountName      = (TextView) convertView.findViewById(R.id.tvSubject);
            //holder.ownerName        = (TextView) convertView.findViewById(R.id.tvMessage);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.agenda_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Agenda agenda = mAgendas.get(position);
        // Is Selected
        holder.checkBox.setChecked(mSelectedAgendas.valueAt(position));
        // text
        holder.displayName.setText(agenda.getDisplayName());
        // Color
        int color = (holder.checkBox.isChecked()) ? mSelectedTextColor : mDefaultTextColor;
        holder.displayName.setTextColor(color);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.checkBox.toggle();
                mSelectedAgendas.put(position, holder.checkBox.isChecked());
            }
        });

        return convertView;
    }

    public SparseBooleanArray getSelectedAgendas() {
        return mSelectedAgendas;
    }
}
