package com.aarafrao.jeerax;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aarafrao.jeerax.databinding.ActivityHomeBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {
    private String ALLOWED_CHARACTERS = "{}[]%^;':,.?/0123456789qwertyuiopasdfghjklzxcvbnm";

    ActivityHomeBinding binding;
    RvAdapter rvAdapter;
    ArrayList<PasswordModel> rvList = new ArrayList<>();
    ArrayList<String> keyList = new ArrayList<>();
    private DatabaseReference mDatabase;

    SharedPreferences.Editor editor;
    ExpandableListView expandableListViewExample;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableTitleList;
    String key;
    HashMap<String, List<PasswordModel>> expandableDetailList;

    private TextView txtMain;
    private String generatedPassword = "";
    private Slider seekbar;
    private MaterialButton btnUsePassword;
    HashMap<String, List<PasswordModel>> listHashMap = new HashMap<>();
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        editor = getSharedPreferences("MAIN_PASSWORD", MODE_PRIVATE).edit();
        ConstraintLayout bottomSheetLayout = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        txtMain = findViewById(R.id.txtMain);
        seekbar = findViewById(R.id.seekbar);
        btnUsePassword = findViewById(R.id.btnUse);


        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        String v1 = getRandomString(10);
        txtMain.setText(v1);
        btnUsePassword.setOnClickListener(v ->
                copyToClipboard(
                        generatedPassword,
                        getApplicationContext()
                )
        );


        seekbar.addOnChangeListener((slider, value, fromUser) -> txtMain.setText(getRandomString((int) value)));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("passwords").child(Constants.ID);
        binding.floatingActionButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, AddPasswordActivity.class)));
        rvList = new ArrayList<>();

        binding.btnGenerate.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        binding.imgLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MAIN_PASSWORD", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = prefs.edit();
            myEdit.putString("main", "No name defined").apply();
            sendToLoginActivity();

        });
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    keyList.add(dataSnapshot.getKey());
                }

                for (int i = 0; i < keyList.size(); i++) {

                    key = keyList.get(i);
                    mDatabase.child(keyList.get(i)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            rvList = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                PasswordModel pd = dataSnapshot.getValue(PasswordModel.class);
                                rvList.add(pd);

                            }
                            expandableListWorking(rvList, snapshot.getKey());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        rvAdapter = new RvAdapter(rvList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(rvAdapter);

    }

    private void sendToLoginActivity() {
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }

    public static void copyToClipboard(String text, Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Password copied!", Toast.LENGTH_SHORT).show();
    }


    private String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        generatedPassword = sb.toString();
        return sb.toString();
    }


    private void expandableListWorking(ArrayList<PasswordModel> rvList, String key) {

        expandableListViewExample = findViewById(R.id.expandableListViewSample);


        List<PasswordModel> valueList = new ArrayList<>();


        for (int k = 0; k < rvList.size(); k++) {
            valueList.add(rvList.get(k));
        }

        listHashMap.put(key, valueList);

        expandableDetailList = listHashMap;
        expandableTitleList = new ArrayList<>(expandableDetailList.keySet());
        expandableListAdapter = new CustomizedExpandableListAdapter(this, expandableTitleList, expandableDetailList);
        expandableListViewExample.setAdapter(expandableListAdapter);

        expandableListViewExample.setOnGroupExpandListener(groupPosition -> Toast.makeText(getApplicationContext(),
                expandableTitleList.get(groupPosition) + " List Expanded.",
                Toast.LENGTH_SHORT).show());

        expandableListViewExample.setOnGroupCollapseListener(groupPosition -> Toast.makeText(getApplicationContext(),
                expandableTitleList.get(groupPosition) + " List Collapsed.",
                Toast.LENGTH_SHORT).show());


        expandableListViewExample.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            showAlertDialogue2(expandableDetailList.get(expandableTitleList.get(groupPosition)).get(childPosition).getApp(),
                    "Email:   " + expandableDetailList.get(expandableTitleList.get(groupPosition)).get(childPosition).getEmail() +
                            "\n\nPassword:   " + expandableDetailList.get(expandableTitleList.get(groupPosition)).get(childPosition).getPassword() +
                            "\n\nHashed:   " + expandableDetailList.get(expandableTitleList.get(groupPosition)).get(childPosition).getHashed(), R.drawable.lock_fill);

            return false;
        });
    }


    private void showAlertDialogue2(String title, String message, int icon) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeActivity.this);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setIcon(icon);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "COPY",
                (dialog, id) -> {

                    String[] copiedData = message.split("Password: ");
                    String[] password = copiedData[1].split("\n");

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(title, password[0]);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Password Copied!", Toast.LENGTH_SHORT).show();
                });

        builder1.setNegativeButton("OK", (dialogInterface, i) -> dialogInterface.cancel());


        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
}