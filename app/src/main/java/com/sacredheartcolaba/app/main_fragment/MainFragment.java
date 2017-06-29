package com.sacredheartcolaba.app.main_fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.sacredheartcolaba.app.MainActivity;
import com.sacredheartcolaba.app.R;
import com.sacredheartcolaba.app.extras.CustomFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

public class MainFragment extends CustomFragment {

    private static final String TAG = "MainFragment";
    //    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MainActivity parentActivity;

    public MainFragment() {
        // Required empty public constructor
    }

    private void showAd(View view) {
        AdView mAdView = (AdView) view.findViewById(R.id.main_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_main, container, false);

        parentActivity = (MainActivity) getActivity();
        parentActivity.setTitle(R.string.app_name);
//        mSwipeRefreshLayout = parentActivity.mSwipeRefreshLayout;

        quoteOfTheDay(fragmentView);
        saintOfTheDay(fragmentView);

        showAd(fragmentView);

        MobileAds.initialize(getActivity(), getString(R.string.banner_ad_unit_id));

        return fragmentView;
    }

    @Override
    public void refresh() {

    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    public void onBackPressed() {
        parentActivity.finish();
    }

    private void quoteOfTheDay(View view) {
        TextView quoteTextTV = (TextView) view.findViewById(R.id.quote_text);
        TextView quoteVerseTV = (TextView) view.findViewById(R.id.quote_verse);

        SharedPreferences preferences = parentActivity.getSharedPreferences(getString(R.string.app_name) + SP_QUOTE_OF_THE_DAY, Context.MODE_PRIVATE);
        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        Log.e(TAG, currentDateTimeString);
        String[] randomQuote = new String[2];

        try {
            randomQuote = getRandomQuote();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        Log.e(TAG, preferences.getString(STRING_QUOTE_TEXT, "null"));

        if (!preferences.contains(STRING_QUOTE_LAST_DATE) || !preferences.contains(STRING_QUOTE_TEXT) || !preferences.contains(STRING_QUOTE_VERSE)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(STRING_QUOTE_LAST_DATE, null);
            editor.putString(STRING_QUOTE_TEXT, null);
            editor.putString(STRING_QUOTE_VERSE, null);
            editor.apply();
        }

        if (!preferences.getString(STRING_QUOTE_LAST_DATE, "").equals(currentDateTimeString)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(STRING_QUOTE_LAST_DATE, currentDateTimeString);
            editor.putString(STRING_QUOTE_TEXT, randomQuote[0]);
            editor.putString(STRING_QUOTE_VERSE, randomQuote[1]);
            editor.apply();
        }

        quoteTextTV.setText(
                preferences.contains(STRING_QUOTE_TEXT) ?
                        preferences.getString(STRING_QUOTE_TEXT, randomQuote[0]).trim() :
                        randomQuote[0].trim()
        );

        quoteVerseTV.setText(
                preferences.contains(STRING_QUOTE_VERSE) ?
                        preferences.getString(STRING_QUOTE_VERSE, randomQuote[1]).trim() :
                        randomQuote[1].trim()
        );
    }

    private String[] getRandomQuote() throws IOException, JSONException {
        InputStream is = parentActivity.getResources().openRawResource(R.raw.quotes);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String temp, json = "";
        while ((temp = br.readLine()) != null)
            json += temp;
        JSONArray saintsJSONArray = new JSONArray(json);
        int countSaints = saintsJSONArray.length();
        int n = new Random().nextInt(countSaints) + 1;
        JSONObject verseJsonObject = saintsJSONArray.getJSONObject(n + 1);
        String[] quote = new String[2];
        quote[0] = verseJsonObject.getString("text");
        quote[1] = verseJsonObject.getString("book_name") + " " + verseJsonObject.getInt("chapter") + ":" + verseJsonObject.getInt("verse");
        return quote;
    }

    private void saintOfTheDay(View view) {
        TextView saintNameTV = (TextView) view.findViewById(R.id.saint_name);

        SharedPreferences preferences = parentActivity.getSharedPreferences(getString(R.string.app_name) + SP_SAINT_OF_THE_DAY, Context.MODE_PRIVATE);
        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        Log.e(TAG, currentDateTimeString);
        String randomSaint = null;

        try {
            randomSaint = getRandomSaint();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        Log.e(TAG, preferences.getString(STRING_SAINT_NAME, "null"));

        if (!preferences.contains(STRING_SAINT_LAST_DATE) || !preferences.contains(STRING_SAINT_NAME)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(STRING_SAINT_LAST_DATE, null);
            editor.putString(STRING_SAINT_NAME, null);
            editor.apply();
        }

        if (!preferences.getString(STRING_SAINT_LAST_DATE, "").equals(currentDateTimeString)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(STRING_SAINT_LAST_DATE, currentDateTimeString);
            editor.putString(STRING_SAINT_NAME, randomSaint);
            editor.apply();
        }

        saintNameTV.setText(
                preferences.contains(STRING_SAINT_NAME) ?
                        preferences.getString(STRING_SAINT_NAME, randomSaint) :
                        randomSaint
        );
    }

    private String getRandomSaint() throws JSONException, IOException {
        InputStream is = parentActivity.getResources().openRawResource(R.raw.saints_and_blesseds);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String temp, json = "";
        while ((temp = br.readLine()) != null)
            json += temp;
        JSONObject sacredHeartJSON = new JSONObject(json);
        JSONArray saintsJSONArray = sacredHeartJSON.getJSONArray("saints");
        int countSaints = saintsJSONArray.length();
        int n = new Random().nextInt(countSaints) + 1;
        return saintsJSONArray.getString(n + 1);
    }

}
