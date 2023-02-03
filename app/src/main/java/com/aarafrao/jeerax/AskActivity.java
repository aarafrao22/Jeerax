package com.aarafrao.jeerax;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

import com.aarafrao.jeerax.databinding.ActivityAskBinding;


public class AskActivity extends AppCompatActivity {

    ActivityAskBinding binding;
    String mainPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPreferences prefs = getSharedPreferences("MAIN_PASSWORD", MODE_PRIVATE);
        mainPass = prefs.getString("main", "No name defined");

        binding.edPasswordMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                new Handler().postDelayed(() -> {

                    if (!mainPass.equals("No name defined")) {
                        if (mainPass.equals(binding.edPasswordMain.getText().toString())) {
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                    } else {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                    }

                }, 3000);

            }
        });
    }
}