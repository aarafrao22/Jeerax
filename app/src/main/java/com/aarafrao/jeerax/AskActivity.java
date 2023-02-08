package com.aarafrao.jeerax;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.aarafrao.jeerax.databinding.ActivityAskBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.Executor;


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
        binding.floatingActionButton.setOnClickListener(v -> {
            if (!mainPass.equals("No name defined")) {

                if (mainPass.equals(binding.edPasswordMain.getText().toString()))
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));

            } else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            }
        });

        binding.edPasswordMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        binding.imgFingerprint.setOnClickListener(v -> authenticate());

    }

    private void authenticate() {
        BiometricPrompt biometricPrompt = new BiometricPrompt(AskActivity.this, runnable -> {
        }, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(AskActivity.this, HomeActivity.class));
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login for my app").setSubtitle("Log in using your biometric credential").setNegativeButtonText("Use account password").build();
        biometricPrompt.authenticate(promptInfo);
    }

}