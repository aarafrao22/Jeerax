package com.aarafrao.jeerax;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aarafrao.jeerax.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.MultiFactorSession;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvDontHave;

    private TextInputEditText edEmail, edPassword;
    private Button btnSignIn;
    private FirebaseAuth firebaseAuth;
    private TextView forgotPassword;
    SharedPreferences.Editor editor;
    FirebaseUser users;
    String verificationId;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();


        binding.BtnVerify.setOnClickListener(view -> {
            if (!binding.edPhone.getText().equals("")) {
                requestCode();
            }
        });
        binding.BtnVerify2.setOnClickListener(view -> {
            if (!binding.edCode.getText().equals("")) {
                finalVerification(verificationId);
            }
        });

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

    private void requestCode() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(binding.edPhone.getText().toString(), 60, TimeUnit.SECONDS, LoginActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                binding.edCode.setVisibility(View.VISIBLE);
                binding.BtnVerify2.setVisibility(View.VISIBLE);
                verificationId = s;
                Toast.makeText(LoginActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();
                binding.BtnVerify.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginActivity.this, "FAILED"+e, Toast.LENGTH_SHORT).show();
                Log.d("FAILED",e.toString());
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //SendToMainActivity
                Toast.makeText(LoginActivity.this, "COMPLETED", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void finalVerification(String s) {
        if (s != null) {
            if (!binding.edCode.getText().toString().equals("")) {
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(s, binding.edCode.getText().toString());
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Verified", Toast.LENGTH_SHORT).show();

                            binding.edCode.setVisibility(View.GONE);
                            binding.edPhone.setVisibility(View.GONE);
                            binding.BtnVerify.setVisibility(View.GONE);
                            binding.BtnVerify2.setVisibility(View.GONE);
                            binding.btnSignIn.setVisibility(View.VISIBLE);

                        }
                    }
                });
            } else {
                Toast.makeText(this, "Enter Code First", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private String phoneNumber;
//    private MultiFactorSession multiFactorSession;
//    PhoneAuthOptions phoneAuthOptions;
//    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
//
//    {
//        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder();
//        builder.setPhoneNumber(phoneNumber);
//        builder.setTimeout(30L, TimeUnit.SECONDS);
//        builder.setMultiFactorSession(multiFactorSession);
//
//        builder.setCallbacks(callbacks);
//        phoneAuthOptions = builder.build();
//    }

    private void saveData() {

    }


    private void sendToMainActivity() {

        if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
            Toast.makeText(LoginActivity.this, "check your email to verify your account", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }

    private void checkEmailAndPassword() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        if (edEmail.getText().toString().matches(emailPattern)) {
            if (edPassword.length() >= 8) {

                disable();

                firebaseAuth.signInWithEmailAndPassword(edEmail.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(task -> {
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