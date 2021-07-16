package com.example.grabtutor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.grabtutor.Model.RewardsRedemptionModel;
import com.example.grabtutor.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddRewardsAdmin extends AppCompatActivity {

    Button add;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rewards_admin);

        add = findViewById(R.id.button_add_rewards);
        databaseReference = FirebaseDatabase.getInstance().getReference("RewardsList").child("Reward 10");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.setValue(new RewardsRedemptionModel("3 Month Spotify Premium", "3000 Points",
                        "https://midiaresearch.com/storage/uploads/blog/images/2018/05/Color-Spotify-Logo.jpg"));
            }
        });
    }


}