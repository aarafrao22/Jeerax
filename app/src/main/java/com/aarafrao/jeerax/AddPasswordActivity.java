package com.aarafrao.jeerax;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private MaterialCheckBox checkBox1, checkBox2, checkBox3;
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
        btnUsePassword = findViewById(R.id.btnUsePassword);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        String v1 = getRandomString((int) 10);
        txtMain.setText(v1);

        btnUsePassword.setOnClickListener(v -> {
            binding.edPassword.setText(generatedPassword);
        });
        seekbar.addOnChangeListener((slider, value, fromUser) -> {
            txtMain.setText(getRandomString((int) value));
        });

        checkBox1 = findViewById(R.id.check1);
        checkBox2 = findViewById(R.id.check2);
        checkBox3 = findViewById(R.id.check3);

        checkBox1.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Toast.makeText(this, "Checked", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "UNChecked", Toast.LENGTH_SHORT).show();

            }
        });
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
        binding.btnGenerate.setOnClickListener(v ->
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        );
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