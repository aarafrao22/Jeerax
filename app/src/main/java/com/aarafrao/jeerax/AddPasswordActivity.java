package com.aarafrao.jeerax;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.aarafrao.jeerax.databinding.ActivityAddPasswordBinding;
import com.aarafrao.jeerax.databinding.BottomSheetLayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

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