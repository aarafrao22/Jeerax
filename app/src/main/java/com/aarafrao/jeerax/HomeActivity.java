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
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.aarafrao.jeerax.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnItemClickListener {

    ActivityHomeBinding binding;
    RvAdapter rvAdapter;
    ArrayList<PasswordModel> rvList = new ArrayList<>();
    ArrayList<String> mainList = new ArrayList<>();
    private DatabaseReference mDatabase;

    SharedPreferences.Editor editor;
    ExpandableListView expandableListViewExample;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableTitleList;
    HashMap<String, List<String>> expandableDetailList;


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
                    mainList.add(dataSnapshot.getKey());
                }

                for (int i = 0; i < mainList.size(); i++) {
                    mDatabase.child(mainList.get(i)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                PasswordModel pd = dataSnapshot.getValue(PasswordModel.class);
                                rvList.add(pd);

                            }
                            expandableListWorking();
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

        rvAdapter = new RvAdapter(rvList, this, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(rvAdapter);

    }

    private void expandableListWorking() {
        expandableListViewExample = findViewById(R.id.expandableListViewSample);

        HashMap<String, List<String>> a = new HashMap<>();
        List<String> a1 = new ArrayList<>();
        for (int i = 0; i < rvList.size(); i++) {
            a1.add(rvList.get(i).getApp());
        }

        for (int i = 0; i < mainList.size(); i++) {
            a.put(mainList.get(i), a1);
        }
        expandableDetailList = a;
        expandableTitleList = new ArrayList<String>(expandableDetailList.keySet());
        expandableListAdapter = new CustomizedExpandableListAdapter(this, expandableTitleList, expandableDetailList);
        expandableListViewExample.setAdapter(expandableListAdapter);

        // This method is called when the group is expanded
        expandableListViewExample.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show();
            }
        });

        // This method is called when the group is collapsed
        expandableListViewExample.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show();
            }
        });


        expandableListViewExample.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition)
                        + " -> "
                        + expandableDetailList.get(
                        expandableTitleList.get(groupPosition)).get(
                        childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
    }

    @Override
    public void onItemClick(int pos) {
        showAlertDialogue2(
                rvList.get(pos).getApp(),
                "Email:   " + rvList.get(pos).getEmail() +
                        "\nPassword:   " + rvList.get(pos).getPassword() +
                        "\nHashed:   " + rvList.get(pos).getHashed(),

                R.drawable.lock_fill);
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
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(title, message);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Password Copied!", Toast.LENGTH_SHORT).show();
                });

        builder1.setNegativeButton("OK", (dialogInterface, i) -> dialogInterface.cancel());


        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
}