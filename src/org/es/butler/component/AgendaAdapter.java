package org.es.butler.component;

import android.content.Context;
import android.graphics.Color;
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
    private LayoutInflater mInflater;

    public AgendaAdapter(Context context, List<Agenda> agendas) {
        mInflater = LayoutInflater.from(context);
        mAgendas = agendas;
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
        CheckBox selected;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.agenda_item, null);
            holder = new ViewHolder();
            // Draw a color rect
            // holder.color        = (ImageView) _convertView.findViewById(R.id.ivThumbnail);
            holder.displayName		= (TextView) convertView.findViewById(R.id.agenda_title);
            //holder.accountName      = (TextView) convertView.findViewById(R.id.tvSubject);
            //holder.ownerName        = (TextView) convertView.findViewById(R.id.tvMessage);
            holder.selected        = (CheckBox) convertView.findViewById(R.id.agenda_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Agenda agenda = mAgendas.get(pos);
        //final Bitmap bmp = card.getThumbBitmap();
        //if (bmp != null && !bmp.isRecycled())
        //    holder.ivThumbnail.setImageBitmap(bmp);
        //else
        //    holder.ivThumbnail.setVisibility(View.GONE);

        holder.displayName.setText(agenda.getDisplayName());
        //holder.tvSubject.setText(card.getSubject());
        //holder.tvMessage.setText(card.getMessage());
        return convertView;
    }
}
