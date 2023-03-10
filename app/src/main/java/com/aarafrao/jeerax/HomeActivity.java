package com.aarafrao.jeerax;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.aarafrao.jeerax.databinding.ActivityHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

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

    HashMap<String, List<PasswordModel>> a = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        editor = getSharedPreferences("MAIN_PASSWORD", MODE_PRIVATE).edit();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("passwords").child(Constants.ID);
        binding.floatingActionButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, AddPasswordActivity.class)));
        rvList = new ArrayList<>();


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

//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    PasswordModel pd = dataSnapshot.getValue(PasswordModel.class);
//                    rvList.add(pd);
//
//                }
//
//                rvAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        //fetch list from Firebase

        rvAdapter = new RvAdapter(rvList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(rvAdapter);

    }

    private void expandableListWorking(ArrayList<PasswordModel> rvList, String key) {

        expandableListViewExample = findViewById(R.id.expandableListViewSample);


        List<PasswordModel> valueList = new ArrayList<>();


        for (int k = 0; k < rvList.size(); k++) {
            valueList.add(rvList.get(k));
        }

        a.put(key, valueList);


//        rvList = new ArrayList<>();
//        valueList = new ArrayList<>();
        expandableDetailList = a;
        expandableTitleList = new ArrayList<>(expandableDetailList.keySet());
        expandableListAdapter = new CustomizedExpandableListAdapter(this, expandableTitleList, expandableDetailList);
        expandableListViewExample.setAdapter(expandableListAdapter);

        // This method is called when the group is expanded
        expandableListViewExample.setOnGroupExpandListener(groupPosition -> Toast.makeText(getApplicationContext(),
                expandableTitleList.get(groupPosition) + " List Expanded.",
                Toast.LENGTH_SHORT).show());

        // This method is called when the group is collapsed
        expandableListViewExample.setOnGroupCollapseListener(groupPosition -> Toast.makeText(getApplicationContext(),
                expandableTitleList.get(groupPosition) + " List Collapsed.",
                Toast.LENGTH_SHORT).show());


        expandableListViewExample.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

//            Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition) + " -> " +
//                    expandableDetailList.get(expandableTitleList.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();

            showAlertDialogue2(
                    expandableDetailList.get(expandableTitleList.get(groupPosition)).get(childPosition).getApp(),
                    "Email:   " + expandableDetailList.get(expandableTitleList.get(groupPosition)).get(childPosition).getEmail() +
                            "\n\nPassword:   " + expandableDetailList.get(expandableTitleList.get(groupPosition)).get(childPosition).getPassword() +
                            "\n\nHashed:   " + expandableDetailList.get(expandableTitleList.get(groupPosition)).get(childPosition).getHashed(),
                    R.drawable.lock_fill);

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