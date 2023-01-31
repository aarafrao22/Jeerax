package com.aarafrao.jeerax;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aarafrao.jeerax.Database.DatabaseHelper;
import com.aarafrao.jeerax.Database.Notification;
import com.aarafrao.jeerax.databinding.ActivityAddPasswordBinding;
import com.aarafrao.jeerax.databinding.BottomSheetLayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.slider.Slider;

import java.util.Random;

public class AddPasswordActivity extends AppCompatActivity {

    private ActivityAddPasswordBinding binding;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetLayoutBinding binding2;
    private MaterialCheckBox checkDIgits, checkAlpha, checkSymbol;
    private TextView txtMain;
    private String generatedPassword = "";
    private Slider seekbar;
    private MaterialButton btnUsePassword;
    private String ALLOWED_CHARACTERS = "{}[]%^;':,.?/0123456789qwertyuiopasdfghjklzxcvbnm";
    private String ALLOWED_CHARACTERS1 = "0123456789";
    private String ALLOWED_CHARACTERS2 = "{}[]%^;':,.?/";
    private String ALLOWED_CHARACTERS3 = "qwertyuiopasdfghjklzxcvbnm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPasswordBinding.inflate(getLayoutInflater());
        binding2 = BottomSheetLayoutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        ConstraintLayout bottomSheetLayout = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        txtMain = findViewById(R.id.txtMain);
        seekbar = findViewById(R.id.seekbar);
        btnUsePassword = findViewById(R.id.btnUse);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        String v1 = getRandomString((int) 10);
        txtMain.setText(v1);

        btnUsePassword.setOnClickListener(v -> binding.edPassword.setText(generatedPassword));

        seekbar.addOnChangeListener((slider, value, fromUser) -> {
            txtMain.setText(getRandomString((int) value));
        });


        checkDIgits = findViewById(R.id.checkDigit);
        checkAlpha = findViewById(R.id.checkAlpha);
        checkSymbol = findViewById(R.id.checkSymbol);

        checkDIgits.setChecked(true);
        checkAlpha.setChecked(true);
        checkSymbol.setChecked(true);
        binding.imgClose.setOnClickListener(v -> finish());
//        checkDIgits.setOnCheckedChangeListener((compoundButton, b) -> {
//            if (b) {
//                Toast.makeText(this, "Checked", Toast.LENGTH_SHORT).show();
//                ALLOWED_CHARACTERS = ALLOWED_CHARACTERS1;
//            } else {
//                ALLOWED_CHARACTERS = "{}[]%^;':,.?/0123456789qwertyuiopasdfghjklzxcvbnm";
//            }
//        });
//        checkAlpha.setOnCheckedChangeListener((compoundButton, b) -> {
//            if (b) {
//                Toast.makeText(this, "Checked", Toast.LENGTH_SHORT).show();
//                ALLOWED_CHARACTERS = ALLOWED_CHARACTERS1;
//            } else {
//                ALLOWED_CHARACTERS = "{}[]%^;':,.?/0123456789qwertyuiopasdfghjklzxcvbnm";
//            }
//        });
//        checkSymbol.setOnCheckedChangeListener((compoundButton, b) -> {
//            if (b) {
//                Toast.makeText(this, "Checked", Toast.LENGTH_SHORT).show();
//                ALLOWED_CHARACTERS = ALLOWED_CHARACTERS1;
//            } else {
//                ALLOWED_CHARACTERS = "{}[]%^;':,.?/0123456789qwertyuiopasdfghjklzxcvbnm";
//            }
//        });

        binding.edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.btnGenerate.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.btnGenerate.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        binding.btnSave.setOnClickListener(v2 -> {

            if (!binding.edName.getText().toString().equals("")) {

                if (!binding.edLogin.getText().toString().equals("")) {

                    if (!binding.edPassword.getText().toString().equals("")) {

                        if (!binding.edComment.getText().toString().equals("")) {

                            //SaveInDatabase On Firebase and ROOM
                            Toast.makeText(this, "Password Saved", Toast.LENGTH_SHORT).show();
                            DatabaseHelper databaseHelper = DatabaseHelper.getDB(getApplicationContext());
                            databaseHelper.notificationDAO().addNotification(new Notification(binding.edName.getText().toString(), binding.edPassword.getText().toString()));
                            Intent intent = new Intent(AddPasswordActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();

                        } else
                            binding.edCommentLayout.setError("Enter");
                    } else
                        binding.edPasswordL.setError("Enter ");
                } else
                    binding.edLoginLayout.setError("Enter MAIL");
            } else {
                binding.edNameLayout.setError("Enter Name");
            }
        });

    }


    private String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        generatedPassword = sb.toString();
        return sb.toString();
    }
}