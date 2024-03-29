package com.aarafrao.jeerax;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aarafrao.jeerax.databinding.ActivityAddPasswordBinding;
import com.aarafrao.jeerax.databinding.BottomSheetLayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AddPasswordActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivityAddPasswordBinding binding;
    private ArrayList<String> paths;
    private int selectedIndex = 0;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetLayoutBinding binding2;
    private MaterialCheckBox checkDIgits, checkAlpha, checkSymbol;
    private DatabaseReference mDatabase;
    private TextView txtMain;
    private String generatedPassword = "";
    private Slider seekbar;
    private MaterialButton btnUsePassword;
    private String ALLOWED_CHARACTERS = "{}[]%^;':,.?/0123456789qwertyuiopasdfghjklzxcvbnm";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPasswordBinding.inflate(getLayoutInflater());
        binding2 = BottomSheetLayoutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        paths = new ArrayList<>();
        paths.add("App");
        paths.add("Website");
        paths.add("+Add");


        mDatabase = FirebaseDatabase.getInstance().getReference();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddPasswordActivity.this,
                android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(this);


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

                            String hashed = null;
                            try {
                                hashed = AESEncryption.encrypt(binding.edPassword.getText().toString());
//                                hashed = "encrypt";
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            long unixTime = System.currentTimeMillis() / 1000L;

                            PasswordModel p = new PasswordModel(
                                    binding.edEmail.getText().toString(),
                                    binding.edName.getText().toString(),
                                    hashed, String.valueOf(unixTime)

                            );

                            mDatabase.child("passwords")
                                    .child(Constants.ID)
                                    .child(paths.get(selectedIndex))
                                    .child(binding.edName.getText().toString())
                                    .setValue(p);

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedIndex = i;
        if (paths.get(i).equals("+Add")) {
            showAddCategoryDialogue();
        }
    }

    private void showAddCategoryDialogue() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddPasswordActivity.this);
        builder1.setTitle("Add Category");
        final EditText input = new EditText(AddPasswordActivity.this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder1.setView(input);
        builder1.setPositiveButton("OK", (dialog, which) -> {
            String text = input.getText().toString();
            paths.add(0, text);
            selectedIndex = 0;
            binding.spinner.setSelection(0);

            //make list on firebase
            dialog.dismiss();
            // Handle the OK button press
        });

        builder1.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();

            // Handle the Cancel button press
        });
        builder1.show();


    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}