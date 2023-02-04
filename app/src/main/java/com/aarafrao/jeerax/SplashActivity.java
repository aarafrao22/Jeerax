package com.aarafrao.jeerax;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    String mainPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Constants.ID = android_id;
        Log.d(TAG, "ID MAIN: " + Constants.ID);

        SharedPreferences prefs = getSharedPreferences("MAIN_PASSWORD", MODE_PRIVATE);
        mainPass = prefs.getString("main", "No name defined");


        new Handler().postDelayed(() -> {

            if (!mainPass.equals("No name defined"))
                startActivity(new Intent(getApplicationContext(), AskActivity.class));
            else
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            finish();
        }, 2000);
    }
}