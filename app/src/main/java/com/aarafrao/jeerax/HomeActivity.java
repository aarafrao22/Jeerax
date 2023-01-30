package com.aarafrao.jeerax;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.aarafrao.jeerax.Database.DatabaseHelper;
import com.aarafrao.jeerax.Database.Notification;
import com.aarafrao.jeerax.databinding.ActivityHomeBinding;

import java.text.ParseException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.floatingActionButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, AddPasswordActivity.class)));


        ArrayList<ItemModel> rvList = new ArrayList<>();
        rvList.add(new ItemModel("Facebook", R.drawable.ic_launcher_foreground));
        rvList.add(new ItemModel("Google", R.drawable.ic_launcher_foreground));
        rvList.add(new ItemModel("YT", R.drawable.ic_launcher_foreground));
        rvList.add(new ItemModel("Facebook", R.drawable.ic_launcher_foreground));
        rvList.add(new ItemModel("Whatsapp", R.drawable.ic_launcher_foreground));

        DatabaseHelper databaseHelper = DatabaseHelper.getDB(getApplicationContext());
        ArrayList<Notification> notifications =
                (ArrayList<Notification>)
                        databaseHelper.notificationDAO().getAllNotifications();


        rvList = new ArrayList<>();

        for (int i = 0; i < notifications.size(); i++) {

            String context = notifications.get(i).getContext();
            int img;
            switch (context) {

                case "1":
                    img = R.drawable.battery_disconnected;
                    break;

                case "2":
                    img = R.drawable.battery_connected;
                    break;

                case "3":
                    img = R.drawable.fence;
                    break;

                case "4":
                    img = R.drawable.ig_on;
                    break;

                case "5":
                    img = R.drawable.ig_of;
                    break;

                default:
                    img = R.drawable.logopie;
                    break;
            }
            try {
                if (rvList.size() > 0) {
                    rvList.add(rvList.size() - i, new NotificationModel(img, notifications.get(i).getTitle(), notifications.get(i).getMessage(), unixToDate(notifications.get(i).getTime())));

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        RvAdapter rvAdapter = new RvAdapter(rvList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(rvAdapter);

    }
}