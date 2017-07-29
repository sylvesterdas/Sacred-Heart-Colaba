package com.sacredheartcolaba.app.main_fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.sacredheartcolaba.app.asynctask.RetrofitClient;
import com.sacredheartcolaba.app.extras.Constants;
import com.sacredheartcolaba.app.extras.CustomFragment;
import com.sacredheartcolaba.app.main_fragment.events.EventsAdapter;
import com.sacredheartcolaba.app.model.Events;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventsFragment extends CustomFragment implements AdapterView.OnItemClickListener, Constants {

    private static final String TAG = "EventsFragment";

    private ListView listView;
    private EventsAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MainActivity parentActivity;

    private ArrayList<Events> eventsList;

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
        refresh();
        return fragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(getActivity(), EventsDetailActivity.class);
        intent.putExtra("id", eventsList.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        retrofit.create(RetrofitClient.class)
                .getAllEvents()
                .enqueue(new Callback<List<Events>>() {
                    @Override
                    public void onResponse(Call<List<Events>> call,
                                           Response<List<Events>> response) {
                        if (response.isSuccessful()) {
                            eventsList = (ArrayList<Events>) response.body();

                            listView.setAdapter(new EventsAdapter(parentActivity, eventsList, getResources()));

                        } else {
                            Toast.makeText(parentActivity, "Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<Events>> call, Throwable t) {
                        Toast.makeText(parentActivity, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    public void onBackPressed() {
        parentActivity.getSupportFragmentManager().popBackStack();
    }

}
