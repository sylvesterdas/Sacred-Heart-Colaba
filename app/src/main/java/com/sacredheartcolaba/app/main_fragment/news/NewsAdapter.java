package com.sacredheartcolaba.app.main_fragment.news;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sacredheartcolaba.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class NewsAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    public Resources res;
    private ArrayList data;

    public NewsAdapter(Activity a, ArrayList d, Resources resLocal) {

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
            vi = inflater.inflate(R.layout.news_list_element, null);

            holder = new ViewHolder();
            holder.dateTV = (TextView) vi.findViewById(R.id.news_list_view_date);
            holder.bodyTV = (TextView) vi.findViewById(R.id.news_list_view_body);
            holder.authorTV = (TextView) vi.findViewById(R.id.news_list_view_author);

            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() > 0) {
            News tempValues = (News) data.get(i);

            int maxLength = res.getInteger(R.integer.max_length_body);

            holder.authorTV.setText(tempValues.getAuthor());
            if (tempValues.getBody().length() >= maxLength)
                holder.bodyTV.setText(String.format("%s...", tempValues.getBody().substring(0, maxLength)));
            else
                holder.bodyTV.setText(tempValues.getBody());

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
        TextView dateTV, bodyTV, authorTV;
    }
}
