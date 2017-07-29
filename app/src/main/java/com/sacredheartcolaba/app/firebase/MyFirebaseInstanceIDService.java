package com.sacredheartcolaba.app.firebase;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.sacredheartcolaba.app.asynctask.RetrofitClient;
import com.sacredheartcolaba.app.extras.Constants;
import com.sacredheartcolaba.app.model.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Sylvester
 * @since 12/4/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements Constants {

    private static final String TAG = "MyFirebaseInsIDService";

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "New Token: " + refreshedToken);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit.create(RetrofitClient.class)
                .updateToken(refreshedToken)
                .enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        if (response.isSuccessful()) {
                            try {
                                Toast.makeText(
                                        MyFirebaseInstanceIDService.this,
                                        "Token Updated: " + response.body().getToken(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            } catch (NullPointerException ignored) {}
                        } else {
                            Toast.makeText(MyFirebaseInstanceIDService.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Toast.makeText(MyFirebaseInstanceIDService.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
