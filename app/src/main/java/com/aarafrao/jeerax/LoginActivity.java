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
//import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.MultiFactorSession;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
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
            users.getMultiFactor().getSession().addOnCompleteListener(new OnCompleteListener<MultiFactorSession>() {
                @Override
                public void onComplete(@NonNull Task<MultiFactorSession> task) {
                    if (task.isSuccessful()) {
                        MultiFactorSession multiFactorSession = task.getResult();
                    }
                }
            });
            PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                private PhoneAuthProvider.ForceResendingToken forceResendingToken;
                private String verificationId;
                private PhoneAuthCredential credential;

                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    // This callback will be invoked in two situations:
                    // 1) Instant verification. In some cases, the phone number can be
                    //    instantly verified without needing to send or enter a verification
                    //    code. You can disable this feature by calling
                    //    PhoneAuthOptions.builder#requireSmsValidation(true) when building
                    //    the options to pass to PhoneAuthProvider#verifyPhoneNumber().
                    // 2) Auto-retrieval. On some devices, Google Play services can
                    //    automatically detect the incoming verification SMS and perform
                    //    verification without user action.
                    this.credential = credential;
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    // This callback is invoked in response to invalid requests for
                    // verification, like an incorrect phone number.
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        // ...
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        // ...
                    }
                    // Show a message and update the UI
                    // ...
                }

                @Override
                public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                    // The SMS verification code has been sent to the provided phone number.
                    // We now need to ask the user to enter the code and then construct a
                    // credential by combining the code with a verification ID.
                    // Save the verification ID and resending token for later use.
                    this.verificationId = verificationId;
                    this.forceResendingToken = token;
                    // ...
                }
            };
//            PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
            // Ask user for the verification code.

//            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);

            saveData();
        });


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