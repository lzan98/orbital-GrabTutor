package com.example.grabtutor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.grabtutor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Rewards extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView listView;
    Button back;
    TextView balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        listView = findViewById(R.id.ListView_rewards);
        back = findViewById(R.id.button_rewards_back);
        balance = findViewById(R.id.rewards_points);

        String[] management =  {"Redeem Rewards", "History"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Rewards.this, android.R.layout.simple_list_item_1, management);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Integer points = snapshot.child("points").getValue(Integer.class);
                    balance.setText(points.toString() +" Points");
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Rewards.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            Intent intent = new Intent(Rewards.this, RewardsRedemptionActivity.class);
            startActivity(intent);
        }
        if (position == 1) {
            Intent intent = new Intent(Rewards.this, RewardsHistoryActivity.class);
            startActivity(intent);
        }
    }


}