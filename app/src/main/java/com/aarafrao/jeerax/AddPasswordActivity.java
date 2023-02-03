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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class AddPasswordActivity extends AppCompatActivity {

    private ActivityAddPasswordBinding binding;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetLayoutBinding binding2;
    private MaterialCheckBox checkDIgits, checkAlpha, checkSymbol;
    private TextView txtMain;
    private DatabaseReference mDatabase;
    private String generatedPassword = "";
    private Slider seekbar;
    private MaterialButton btnUsePassword;
    private String ALLOWED_CHARACTERS = "{}[]%^;':,.?/0123456789qwertyuiopasdfghjklzxcvbnm";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPasswordBinding.inflate(getLayoutInflater());
        binding2 = BottomSheetLayoutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        mDatabase = FirebaseDatabase.getInstance().getReference();

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


        binding.btnGenerate.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        binding.btnSave.setOnClickListener(v2 -> {

            if (!binding.edEmail.getText().toString().equals("")) {

                if (!binding.edName.getText().toString().equals("")) {

                    if (!binding.edPassword.getText().toString().equals("")) {

                        if (!binding.edComment.getText().toString().equals("")) {

                            //SaveInDatabase On Firebase and ROOM

                            Toast.makeText(this, "Password Saved", Toast.LENGTH_SHORT).show();
                            String uname = binding.edEmail.getText().toString();
                            String[] u_name = uname.split("@");

                            PasswordModel p = new PasswordModel(
                                    binding.edEmail.getText().toString(),
                                    binding.edPassword.getText().toString(),
                                    binding.edName.getText().toString()
                            );

                            mDatabase.child("passwords")
                                    .child(u_name[0])
                                    .child(binding.edName.getText().toString())
                                    .setValue(p);

//                            DatabaseHelper databaseHelper = DatabaseHelper.getDB(getApplicationContext());
//                            databaseHelper.notificationDAO().addNotification(new
//                                    Notification(
//                                            binding.edName.getText().toString(),
//                                            binding.edPassword.getText().toString()
//                                    )
//                            );
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