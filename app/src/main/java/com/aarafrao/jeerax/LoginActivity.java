package com.aarafrao.jeerax;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvDontHave;
    private TextInputEditText edEmail, edPassword;
    private Button btnSignIn;
    private FirebaseAuth firebaseAuth;
    private TextView forgotPassword;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        btnSignIn.setOnClickListener(v -> {
            checkEmailAndPassword();
            saveData();
        });
    }

    private void saveData() {

    }


    private void sendToMainActivity(String s) {
        startActivity(new Intent(this, HomeActivity.class).putExtra("name", s));
        finish();
    }

    private void checkEmailAndPassword() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        if (edEmail.getText().toString().matches(emailPattern)) {
            if (edPassword.length() >= 8) {

                disable();

                firebaseAuth.signInWithEmailAndPassword(edEmail.getText().toString(),
                                edPassword.getText().toString())
                        .addOnCompleteListener(task -> {
                            FirebaseUser user = task.getResult().getUser();
                            if (task.isSuccessful()) {
                                editor.putString("main", edPassword.getText().toString());
                                editor.apply();
                                sendToMainActivity(user.getDisplayName());
                            } else {
                                Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_LONG);

                            }
                        });
            } else {
                Toast.makeText(this, "Incorrect Email or Password", Toast.LENGTH_SHORT);
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
        tvDontHave = findViewById(R.id.sign_in_dont_have);
        tvDontHave.setOnClickListener(this);

        edEmail = findViewById(R.id.sign_in_email);
        edPassword = findViewById(R.id.sign_in_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        editor = getSharedPreferences("MAIN_PASSWORD", MODE_PRIVATE).edit();

        forgotPassword = findViewById(R.id.sign_in_forgot);

        forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_dont_have:
                startActivity(new Intent(this, SignupActivity.class));
                break;
        }
    }
}