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

import com.sacredheartcolaba.app.MainActivity;
import com.sacredheartcolaba.app.NewsDetailsActivity;
import com.sacredheartcolaba.app.R;
import com.sacredheartcolaba.app.asynctask.RetrofitClient;
import com.sacredheartcolaba.app.extras.CustomFragment;
import com.sacredheartcolaba.app.main_fragment.news.NewsAdapter;
import com.sacredheartcolaba.app.model.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsFragment extends CustomFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "NewsFragment";

    private ListView listView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MainActivity parentActivity;

    private SharedPreferences preferences;

    private ArrayList<News> newsList;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_news, container, false);

        parentActivity = (MainActivity) getActivity();
        parentActivity.setTitle(R.string.main_nav_news);
        mSwipeRefreshLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.swiperefresh);

        listView = (ListView) fragmentView.findViewById(R.id.news_list_view);
        listView.setOnItemClickListener(this);

        preferences = parentActivity.getSharedPreferences(getString(R.string.app_name) + SP_NEWS, Context.MODE_PRIVATE);

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
    public void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://sacredheartcolaba.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        retrofit.create(RetrofitClient.class)
                .getAllNews()
                .enqueue(new Callback<List<com.sacredheartcolaba.app.model.News>>() {
                    @Override
                    public void onResponse(Call<List<com.sacredheartcolaba.app.model.News>> call,
                                           Response<List<com.sacredheartcolaba.app.model.News>> response) {
                        if (response.isSuccessful()) {
                            newsList = (ArrayList<News>) response.body();

                            listView.setAdapter(new NewsAdapter(parentActivity, newsList, getResources()));

                        } else {
                            Toast.makeText(parentActivity, "Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<com.sacredheartcolaba.app.model.News>> call, Throwable t) {
                        Toast.makeText(parentActivity, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent detailViewIntent = new Intent(parentActivity, NewsDetailsActivity.class);
        detailViewIntent.putExtra("id", newsList.get(position).getId());
        startActivity(detailViewIntent);
    }
}