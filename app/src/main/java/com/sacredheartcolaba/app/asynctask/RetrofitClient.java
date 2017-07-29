package com.sacredheartcolaba.app.asynctask;

import com.sacredheartcolaba.app.model.News;
import com.sacredheartcolaba.app.model.Events;
import com.sacredheartcolaba.app.model.Token;

import org.json.JSONArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Sylvester Das
 * @since 7/24/2017.
 */

public interface RetrofitClient {

    @GET("getNews")
    Call<List<News>> getAllNews();

    @GET("getNews/{news}")
    Call<News> getNews(@Path("news") int id);

    @GET("getEvents")
    Call<List<Events>> getAllEvents();

    @GET("getEvents/{event}")
    Call<Events> getEvent(@Path("event") int id);

    @POST("token")
    Call<Token> updateToken(@Query("token") String token);
}