package com.sacredheartcolaba.app;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.sacredheartcolaba.app.extras.Constants;
import com.sacredheartcolaba.app.extras.CustomFragment;
import com.sacredheartcolaba.app.main_fragment.ContactFragment;
import com.sacredheartcolaba.app.main_fragment.EventsFragment;
import com.sacredheartcolaba.app.main_fragment.MainFragment;
import com.sacredheartcolaba.app.main_fragment.NewsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Constants {

    private static final String TAG = "MainActivity";
    private CustomFragment customFragment;
    private DrawerLayout drawer;

    private GoogleApiClient client;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrash.log("My first Android non-fatal error");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupWindowAnimations();

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.logEvent(getString(R.string.app_name), null);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            Fragment newFragment = new MainFragment();

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.add(R.id.main_fragment, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }

        if (getIntent().hasExtra(EXTRA_KEY_NOTIFICATION))
            hasNotification(getIntent().getStringExtra(EXTRA_KEY_NOTIFICATION));

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void hasNotification(String extra) {
        CustomFragment fragment = new MainFragment();
        if (extra.equals(EXTRA_VALUE_NEWS)) {
            fragment = new NewsFragment();
        } else if (extra.equals(EXTRA_VALUE_EVENTS)) {
            fragment = new EventsFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.main_fragment, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    void myUpdateOperation() {
        customFragment = (CustomFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        customFragment.refresh();
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            customFragment = (CustomFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
            customFragment.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            // Check if user triggered a refresh:
            case R.id.menu_refresh:
                Log.i(TAG, "Refresh menu item selected");

                // Start the refresh background task.
                // This method calls setRefreshing(false) when it's finished.
                myUpdateOperation();

                return true;
            /*case R.id.action_settings:
                return true;*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment newFragment;

        FragmentManager manager = getSupportFragmentManager();
        int count = manager.getBackStackEntryCount();
        if (count > 1) {
            manager.popBackStack();
        }
        count = manager.getBackStackEntryCount();
        Log.e("Count", String.valueOf(count));

        switch (id) {
            case R.id.nav_news:
                newFragment = new NewsFragment();
                break;
            case R.id.nav_events:
                newFragment = new EventsFragment();
                break;
            /*case R.id.nav_settings:
                newFragment = new MainFragment();
                break;*/
            /*case R.id.nav_help:
                newFragment = new MainFragment();
                break;*/
            case R.id.nav_contact_us:
                newFragment = new ContactFragment();
                break;
            default:
                newFragment = new MainFragment();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.main_fragment, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openNews(View view) {
        Fragment newFragment = new NewsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.main_fragment, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    public void openEvents(View view) {
        Fragment newFragment = new EventsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.main_fragment, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    public void email(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + getString(R.string.support_email)));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "Mail account not configured", Toast.LENGTH_SHORT).show();
        }
    }

    public void phone(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + getString(R.string.contact_no)));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "No Application to Dial", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Sacred Heart Church")
                .setUrl(Uri.parse("http://sacredheartcolaba/app"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
