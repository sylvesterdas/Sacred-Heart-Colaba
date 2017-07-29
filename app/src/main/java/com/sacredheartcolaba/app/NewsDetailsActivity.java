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
import com.sacredheartcolaba.app.model.News;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsDetailsActivity extends AppCompatActivity {

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        setupWindowAnimations();

        setTitle("NEWS");

        id = getIntent().getIntExtra("id", 0);

        refresh();
    }

    private void refresh() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://sacredheartcolaba.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        retrofit.create(RetrofitClient.class)
                .getNews(id)
                .enqueue(new Callback<News>() {
                    @Override
                    public void onResponse(Call<News> call,
                                           Response<News> response) {
                        if (response.isSuccessful()) {
                            try {
                                News news = response.body();

                                if (news.getTitle().length() >= 30)
                                    setTitle(String.format("%s...", news.getTitle().substring(0, 30)));
                                else
                                    setTitle(news.getTitle());

                                CharSequence uploadedOn = getString(R.string.uploaded_on);

                                Date date;
                                try {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                    //Format to match actual String to parse
                                    date = format.parse(news.getCreated_at());
                                    SimpleDateFormat newFormat = new SimpleDateFormat("d MMMM, yyyy", Locale.US);
                                    ((TextView) findViewById(R.id.news_detail_date)).setText(String.format("%s %s", uploadedOn, newFormat.format(date)));
                                } catch (ParseException e) {
                                    ((TextView) findViewById(R.id.news_detail_date)).setText(String.format("%s %s", uploadedOn, news.getCreated_at()));
                                }


                                ((TextView) findViewById(R.id.news_detail_title)).setText(news.getTitle() + "");
                                ((TextView) findViewById(R.id.news_detail_text)).setText(news.getBody() + "");

                            } catch (NullPointerException e) {
                                finish();
                            }

                        } else {
                            Toast.makeText(NewsDetailsActivity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<com.sacredheartcolaba.app.model.News> call, Throwable t) {
                        Toast.makeText(NewsDetailsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
