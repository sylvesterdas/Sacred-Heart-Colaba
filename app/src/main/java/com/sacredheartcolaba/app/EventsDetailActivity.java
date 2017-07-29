package com.sacredheartcolaba.app;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;
import android.widget.TextView;
import android.widget.Toast;

import com.sacredheartcolaba.app.asynctask.RetrofitClient;
import com.sacredheartcolaba.app.model.Events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventsDetailActivity extends AppCompatActivity {

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_detail);
        setupWindowAnimations();

        setTitle(getString(R.string.main_nav_events));

        id = getIntent().getIntExtra("id", 0);
        refresh();
    }

    private void refresh() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://sacredheartcolaba.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        retrofit.create(RetrofitClient.class)
                .getEvent(id)
                .enqueue(new Callback<Events>() {
                    @Override
                    public void onResponse(Call<Events> call,
                                           Response<Events> response) {
                        if (response.isSuccessful()) {
                            try {

                                Events event = response.body();

                                int maxLength = getResources().getInteger(R.integer.max_length_body);

                                if (event.getTitle().length() >= maxLength)
                                    setTitle(String.format("%s...", event.getTitle().substring(0, maxLength)));
                                else
                                    setTitle(event.getTitle());


                                CharSequence uploadedOn = "Event On:";

                                Date date;
                                try {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                    //Format to match actual String to parse
                                    date = format.parse(event.getDate());
                                    SimpleDateFormat newFormat = new SimpleDateFormat("d MMMM, yyyy", Locale.US);
                                    ((TextView) findViewById(R.id.events_detail_date)).setText(String.format("%s %s", uploadedOn, newFormat.format(date)));
                                } catch (ParseException e) {
                                    ((TextView) findViewById(R.id.events_detail_date)).setText(String.format("%s %s", uploadedOn, event.getDate()));
                                }

                                ((TextView) findViewById(R.id.events_detail_title)).setText(event.getTitle() + "");
                                ((TextView) findViewById(R.id.events_detail_text)).setText(event.getBody() + "");

                            } catch (NullPointerException e) {
                                finish();
                            }
                        } else {
                            Toast.makeText(EventsDetailActivity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Events> call, Throwable t) {
                        Toast.makeText(EventsDetailActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
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
