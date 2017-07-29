package com.sacredheartcolaba.app.main_fragment.events;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sacredheartcolaba.app.R;
import com.sacredheartcolaba.app.model.Events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class EventsAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    public Resources res;
    private ArrayList data;

    public EventsAdapter(Activity a, ArrayList d, Resources resLocal) {

        /********** Take passed values **********/
        data = d;
        res = resLocal;

        /***********  Layout inflater to call external xml layout () ***********/
        inflater = (LayoutInflater) a.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        ViewHolder holder;

        if (view == null) {
            vi = inflater.inflate(R.layout.events_list_element, null);

            holder = new ViewHolder();
            holder.dateTV = (TextView) vi.findViewById(R.id.events_list_view_date);
            holder.titleTV = (TextView) vi.findViewById(R.id.events_list_view_title);

            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() > 0) {
            Events tempValues = (Events) data.get(i);

            int maxLength = res.getInteger(R.integer.max_length_body);
            Log.w(getClass().getName(), tempValues.toString());

            if (tempValues.getBody().length() >= maxLength)
                holder.titleTV.setText(String.format("%s...", tempValues.getTitle().substring(0, maxLength)));
            else
                holder.titleTV.setText(tempValues.getTitle());

            Date date;
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                //Format to match actual String to parse
                date = format.parse(tempValues.getDate());
                SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US);
                holder.dateTV.setText(newFormat.format(date));
            } catch (ParseException e) {
                holder.dateTV.setText(tempValues.getDate());
            }
        }

        return vi;
    }

    private static class ViewHolder {
        TextView dateTV, titleTV;
    }
}
