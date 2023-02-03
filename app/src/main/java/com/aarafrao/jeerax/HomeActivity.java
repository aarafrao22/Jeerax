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
import android.os.Bundle;
import android.widget.Toast;

import com.aarafrao.jeerax.databinding.ActivityHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements OnItemClickListener {

    ActivityHomeBinding binding;
    RvAdapter rvAdapter;
    ArrayList<ItemModel> rvList = new ArrayList<>();
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.floatingActionButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, AddPasswordActivity.class)));

//        DatabaseHelper databaseHelper = DatabaseHelper.getDB(getApplicationContext());
//        ArrayList<Notification> notifications =
//                (ArrayList<Notification>)
//                        databaseHelper.notificationDAO().getAllNotifications();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("passwords").child("aarafrao22");

        rvList = new ArrayList<>();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ItemModel pd = dataSnapshot.getValue(ItemModel.class);
                    rvList.add(pd);
                }
                rvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //fetch list from Firebase

        rvAdapter = new RvAdapter(rvList, this, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(rvAdapter);

    }

    @Override
    public void onItemClick(int pos) {
        showAlertDialogue2(rvList.get(pos).getApp(), "The Password is:" + rvList.get(pos).getPassword(), R.drawable.lock_fill);
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