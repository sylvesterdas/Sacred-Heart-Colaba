package com.sacredheartcolaba.app.main_fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sacredheartcolaba.app.EventsDetailActivity;
import com.sacredheartcolaba.app.MainActivity;
import com.sacredheartcolaba.app.R;
import com.sacredheartcolaba.app.database.EventsTable;
import com.sacredheartcolaba.app.extras.CustomFragment;
import com.sacredheartcolaba.app.extras.DataModel;
import com.sacredheartcolaba.app.main_fragment.events.Events;
import com.sacredheartcolaba.app.main_fragment.events.EventsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class EventsFragment extends CustomFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "EventsFragment";

    private ListView listView;
    private EventsAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MainActivity parentActivity;

    private SharedPreferences preferences;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_events, container, false);

        parentActivity = (MainActivity) getActivity();
        parentActivity.setTitle(R.string.main_nav_events);
        mSwipeRefreshLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.swiperefresh);

        preferences = parentActivity.getSharedPreferences(getString(R.string.app_name) + SP_EVENTS, Context.MODE_PRIVATE);

        listView = (ListView) fragmentView.findViewById(R.id.events_list_view);
        listView.setOnItemClickListener(this);
        EventsTable table = new EventsTable(getActivity());
        if (table.getRowCount() > 0)
            getDataFromDatabase();
        else
            refresh();

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        refresh();
                    }
                }
        );
        return fragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Events events = (Events) adapter.getItem(i);
        Intent intent = new Intent(getActivity(), EventsDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DataModel.INTENT_EXTRA_DATA, events);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    void getDataFromDatabase() {
        mSwipeRefreshLayout.setRefreshing(true);

        List<Events> eventsList = new EventsTable(parentActivity).getAllRows();
        ArrayList<Events> eventsArrayList = new ArrayList<>();
        for (Events events : eventsList) {
            eventsArrayList.add(events);
            Log.e(TAG, "getDataFromDatabase: " + events.toString());
        }
        adapter = new EventsAdapter(EventsFragment.this.getActivity(), eventsArrayList, getActivity().getResources());
        listView.setAdapter(adapter);

        preferences.edit().putBoolean(BOOLEAN_NEW_EVENT, false).apply();

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new FetchEvents(getActivity()).execute();
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    public void onBackPressed() {
        parentActivity.getSupportFragmentManager().popBackStack();
    }

    private class FetchEvents extends AsyncTask<Void, Void, String> {

        private static final String TAG_FETCH_EVENTS = "FetchEvents";
        URL mURL = null;
        Context mContext;
        private String BASE_URL;
        private String GET_EVENTS_API;
        private String church_id;

        FetchEvents(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            String URI = null;
            try {
                BASE_URL = mContext.getString(R.string.BASE_URL);
                GET_EVENTS_API = mContext.getString(R.string.GET_EVENTS_API);
                church_id = mContext.getString(R.string.church_id);
                URI = BASE_URL + GET_EVENTS_API;
                mURL = new URL(URI);
            } catch (MalformedURLException e) {
                Log.e(TAG_FETCH_EVENTS, URI);
                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
                cancel(true);
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection httpURLConnection;
            Log.e(TAG_FETCH_EVENTS, mURL.toString());
            try {
                httpURLConnection = (HttpURLConnection) mURL.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("church_id", church_id);
                Log.e("params", postDataParams.toString());

                String data = getPostDataString(postDataParams);

                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);

                writer.flush();
                writer.close();
                os.close();

                int responseCode = httpURLConnection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder("");
                    String line;

                    if ((line = in.readLine()) != null) {

                        sb.append(line);
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return null;
                }
            } catch (IOException | JSONException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty() && !s.trim().equalsIgnoreCase("")) {
                try {
                    Log.e(TAG_FETCH_EVENTS, s);

                    JSONObject replyObject = new JSONObject(s);
                    if (replyObject.getBoolean("status")) {
                        JSONArray messageArray = replyObject.getJSONArray("message");

                        for (int i = 0; i < messageArray.length(); i++) {
                            JSONObject eventsObject = messageArray.getJSONObject(i);
                            Events events = new Events(
                                    eventsObject.getInt("id"),
                                    eventsObject.getString("events_body"),
                                    eventsObject.getString("name"),
                                    eventsObject.getString("modified_on")
                            );
                            if (new EventsTable(parentActivity).getOneRow(events.getId()) == null) {
                                new EventsTable(parentActivity).addRow(events);
                            } else {
                                new EventsTable(parentActivity).updateRow(events);
                            }
                        }
                        getDataFromDatabase();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (mSwipeRefreshLayout.isRefreshing())
                        mSwipeRefreshLayout.setRefreshing(false);
                }
            } else {
                Toast.makeText(getActivity(), R.string.check_internet, Toast.LENGTH_SHORT).show();
                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
            }
        }

        String getPostDataString(JSONObject params) throws UnsupportedEncodingException, JSONException {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }
}
