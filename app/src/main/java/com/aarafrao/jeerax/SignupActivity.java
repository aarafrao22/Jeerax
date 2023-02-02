package com.aarafrao.jeerax;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvAlreadyHave;
    private TextInputEditText edEmail, edPassword, edName;
    private Button btnSignUp;

    FirebaseDatabase rootNode;
    MaterialCheckBox checkA12, check1no, check1lowercase, check1UpperCase, check1SpecialCharacter;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();

        clickListeners();

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

        edName.addTextChangedListener(new TextWatcher() {
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
//                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSignUp.setOnClickListener(v -> {
            if (isValid(edPassword.getText().toString())) {
                Toast.makeText(SignupActivity.this, "Password is GOOD", Toast.LENGTH_SHORT).show();
                check1UpperCase.setChecked(true);
                checkA12.setChecked(true);
                check1SpecialCharacter.setChecked(true);
                check1lowercase.setChecked(true);
                check1no.setChecked(true);
                checkEmailAndPassword();
            } else {
                Toast.makeText(SignupActivity.this, "Password is Weak", Toast.LENGTH_SHORT).show();

                if (hasNumberRegex(edPassword.getText().toString())) {
                    check1no.setChecked(true);

                    if (hasLowercase(edPassword.getText().toString())) {
                        check1lowercase.setChecked(true);

                        if (hasUppercase(edPassword.getText().toString())) {
                            check1UpperCase.setChecked(true);

                            if (hasSpecialCharacter(edPassword.getText().toString())) {
                                check1SpecialCharacter.setChecked(true);

                                if (isLongerThan11(edPassword.getText().toString())) {
                                    checkA12.setChecked(true);


                                }

                            }

                        }
                    }
                }
            }

        });
    }

    public static boolean isLongerThan11(String password) {
        Pattern pattern = Pattern.compile(".{8,}");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public static boolean hasSpecialCharacter(String password) {
        Pattern pattern = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public static boolean hasLowercase(String password) {
        Pattern pattern = Pattern.compile("[a-z]");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public static boolean hasUppercase(String password) {
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public static boolean hasNumberRegex(String password) {
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public static boolean isValid(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%&!$*])[a-zA-Z0-9@#$%&!$*]{8,15}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private void saveData(String email, String name, String pass) {
        SharedPreferences prefs = this.getSharedPreferences("Details", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = prefs.edit();

        myEdit.putString("Email", email);
        myEdit.putString("Name", name);
        myEdit.putString("pass", pass);
        myEdit.apply();
        Toast.makeText(this, "DataSaved", Toast.LENGTH_SHORT).show();
    }

    private void clickListeners() {
        tvAlreadyHave.setOnClickListener(this);
    }

    private void checkEmailAndPassword() {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        if (edEmail.getText().toString().matches(emailPattern)) {

            btnSignUp.setEnabled(false);
            btnSignUp.setTextColor(Color.argb(50, 255, 255, 255));

            String mail = edEmail.getText().toString();
            String pass = edPassword.getText().toString();
            saveData(mail, edName.getText().toString(), pass);

            firebaseAuth.createUserWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
//                                updateUI(user);
                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("users");

                            UserHelper userHelper = new UserHelper(
                                    edName.getText().toString(),
                                    edPassword.getText().toString(),
                                    edEmail.getText().toString()
                            );

                            String uname = edEmail.getText().toString();
                            String[] u_name = uname.split("@");

                            reference.child(u_name[0]).setValue(userHelper);
                            sendToMainActivity(edName.getText().toString());

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void sendToMainActivity(String s) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    private void checkInputs() {
        if (!TextUtils.isEmpty(edEmail.getText())) {
            if (!TextUtils.isEmpty(edName.getText())) {
                if (!TextUtils.isEmpty(edPassword.getText()) && edPassword.length() >= 8) {
                    setBtnEnabled();
                } else {
//                    setBtnDisabled();
                }
            } else {
//                setBtnDisabled();
            }
        } else {
//            setBtnDisabled();
        }
    }

    public void setBtnEnabled() {
        btnSignUp.setEnabled(true);
        btnSignUp.setTextColor(Color.rgb(255, 255, 255));
    }

    public void setBtnDisabled() {
        btnSignUp.setEnabled(false);
        btnSignUp.setTextColor(Color.argb(50, 255, 255, 255));
    }

    private void initViews() {
        tvAlreadyHave = findViewById(R.id.sign_up_already_have);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edMasterPass);
        edName = findViewById(R.id.edReminder);
        btnSignUp = findViewById(R.id.btnContinue);
        checkA12 = findViewById(R.id.checkA12);
        check1no = findViewById(R.id.check1no);
        check1SpecialCharacter = findViewById(R.id.check1SpecialCharacter);
        check1lowercase = findViewById(R.id.check1lowercase);
        check1UpperCase = findViewById(R.id.check1UpperCase);


        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_up_already_have:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

}