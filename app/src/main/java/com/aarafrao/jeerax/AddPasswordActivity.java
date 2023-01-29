package com.aarafrao.jeerax;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import com.aarafrao.jeerax.databinding.ActivityAddPasswordBinding;
import com.aarafrao.jeerax.databinding.BottomSheetLayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.slider.Slider;

import java.util.Random;

public class AddPasswordActivity extends AppCompatActivity {

    ActivityAddPasswordBinding binding;
    private BottomSheetBehavior bottomSheetBehavior;
    BottomSheetLayoutBinding binding2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ConstraintLayout bottomSheetLayout = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        binding2.seekbar.addOnChangeListener((slider, value, fromUser) -> {
            binding2.txtMain.setText(getRandomString((int) value));
        });
        binding.edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.btnGenerate.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.btnGenerate.setOnClickListener(v ->
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        );
    }

    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
    private static final String ALLOWED_CHARACTERS1 = "0123456789qwertyuiopasdfghjklzxcvbnm";
    private static final String ALLOWED_CHARACTERS2 = "0123456789qwertyuiopasdfghjklzxcvbnm;':,.?/";

    private static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
}