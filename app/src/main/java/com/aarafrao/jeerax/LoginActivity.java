package com.aarafrao.jeerax;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aarafrao.jeerax.databinding.ActivityLoginBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText edEmail, edPassword;
    private Button btnSignIn;
    private FirebaseAuth firebaseAuth;
    SharedPreferences.Editor editor;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();



        edEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSignIn.setOnClickListener(v -> checkEmailAndPassword());


    }

    private void sendToMainActivity() {

        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).isEmailVerified()) {
            startActivity(new Intent(this, VerificationActivity.class));
        } else {
            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
            Toast.makeText(LoginActivity.this, "check your email to verify your account", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }

    private void checkEmailAndPassword() {
        String emailPattern = "[a-zA-Z\\d._-]+@[a-z]+.[a-z]+";
        if (Objects.requireNonNull(edEmail.getText()).toString().matches(emailPattern)) {
            if (edPassword.length() >= 8) {

                disable();

                firebaseAuth.signInWithEmailAndPassword(edEmail.getText().toString(), Objects.requireNonNull(edPassword.getText()).toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        editor.putString("main", edPassword.getText().toString());
                        editor.apply();
                        sendToMainActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect Email or Password", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void enable() {
        btnSignIn.setEnabled(true);
        btnSignIn.setTextColor(Color.rgb(255, 255, 255));
    }

    private void disable() {

        btnSignIn.setEnabled(false);
        btnSignIn.setTextColor(Color.argb(50, 255, 255, 255));

    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(edEmail.getText())) {
            if (!TextUtils.isEmpty(edPassword.getText())) {
                enable();
            } else {
                disable();
            }
        } else {
            disable();
        }
    }

    private void initViews() {
        TextView tvDontHave = findViewById(R.id.sign_in_dont_have);
        tvDontHave.setOnClickListener(this);

        edEmail = findViewById(R.id.sign_in_email);
        edPassword = findViewById(R.id.sign_in_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        editor = getSharedPreferences("MAIN_PASSWORD", MODE_PRIVATE).edit();

        TextView forgotPassword = findViewById(R.id.sign_in_forgot);

        forgotPassword.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_dont_have) {
            startActivity(new Intent(this, SignupActivity.class));
        }
    }
}