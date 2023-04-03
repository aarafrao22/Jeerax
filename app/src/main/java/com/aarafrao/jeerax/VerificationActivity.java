package com.aarafrao.jeerax;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aarafrao.jeerax.databinding.ActivityVerificationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    ActivityVerificationBinding binding;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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

    }


    private void requestCode() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(binding.edPhone.getText().toString(), 60, TimeUnit.SECONDS, VerificationActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                binding.phoneConstraint.setVisibility(View.GONE);
                binding.codeConstraint.setVisibility(View.VISIBLE);
                verificationId = s;
                Toast.makeText(VerificationActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(VerificationActivity.this, "FAILED: " + e, Toast.LENGTH_SHORT).show();
                Log.d("FAILED", e.toString());
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //SendToMainActivity
                Toast.makeText(VerificationActivity.this, "COMPLETED", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(VerificationActivity.this, "Verified", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(VerificationActivity.this, HomeActivity.class));
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Enter Code First", Toast.LENGTH_SHORT).show();
            }
        }
    }
}