package com.aarafrao.jeerax;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    String mainPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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