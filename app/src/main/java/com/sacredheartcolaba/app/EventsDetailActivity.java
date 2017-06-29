package com.sacredheartcolaba.app;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;
import android.widget.TextView;

import com.sacredheartcolaba.app.extras.DataModel;
import com.sacredheartcolaba.app.main_fragment.events.Events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_detail);
        setupWindowAnimations();

        setTitle(getString(R.string.main_nav_events));

        Bundle bundle = getIntent().getExtras();
        Events events = (Events) bundle.getSerializable(DataModel.INTENT_EXTRA_DATA);

        CharSequence uploadedOn = getString(R.string.uploaded_on);

        Date date;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            //Format to match actual String to parse
            date = format.parse(events.getDate());
            SimpleDateFormat newFormat = new SimpleDateFormat("d MMMM, yyyy", Locale.US);
            ((TextView) findViewById(R.id.events_detail_date)).setText(String.format("%s %s", uploadedOn, newFormat.format(date)));
        } catch (ParseException e) {
            ((TextView) findViewById(R.id.events_detail_date)).setText(String.format("%s %s", uploadedOn, events.getDate()));
        }

        ((TextView) findViewById(R.id.events_detail_text)).setText(events.getBody() + "");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(3000);
        getWindow().setEnterTransition(fade);
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }
}
