package com.sacredheartcolaba.app.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.sacredheartcolaba.app.R;

/**
 * @author Sylvester Das
 * @since 20-03-2017
 */

public class GetNews extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private String[] data;
    private String church_id;

    public GetNews(Context mContext, String[] data) {
        this.mContext = mContext;
        this.data = data;
        church_id = mContext.getResources().getString(R.string.church_id);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
