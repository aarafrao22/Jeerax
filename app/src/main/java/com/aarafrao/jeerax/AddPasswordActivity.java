package com.aarafrao.jeerax;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

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


    @RequiresApi(api = Build.VERSION_CODES.O)
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
                            String secKey = null;
                            try {
                                secKey = generateSecretKey(binding.edPassword.getText().toString());
                            } catch (NoSuchAlgorithmException e) {
                                throw new RuntimeException(e);
                            }
                            String hashed = null;
                            try {
                                hashed = encrypt(binding.edPassword.getText().toString(), secKey);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            long unixTime = System.currentTimeMillis() / 1000L;

                            PasswordModel p = new PasswordModel(
                                    binding.edEmail.getText().toString(),
                                    binding.edPassword.getText().toString(),
                                    binding.edName.getText().toString(),
                                    hashed, String.valueOf(unixTime)

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

    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String generateSecretKey(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(String text, String secretKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
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