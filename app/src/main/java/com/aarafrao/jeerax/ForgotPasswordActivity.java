package com.aarafrao.jeerax;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText sign_in_email;
    MaterialButton btn_sign_in;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        initViews();
    }

    private void initViews() {
        sign_in_email = findViewById(R.id.sign_in_email);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_sign_in.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_in:
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
                if (sign_in_email.getText().toString().matches(emailPattern)) {
                    Log.d(TAG, "onClick: " + sign_in_email.getText().toString());
                    forgotEve();
                } else {
                    sign_in_email.setError("Invalid Email");
                }
                break;
        }
    }

    private void forgotEve() {
//        firebaseAuth.pass
        firebaseAuth.sendPasswordResetEmail(sign_in_email.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, "EMAIL SENT SUCCESSFULLY", Toast.LENGTH_SHORT).show();

                    } else {
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(ForgotPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                });
    }
}