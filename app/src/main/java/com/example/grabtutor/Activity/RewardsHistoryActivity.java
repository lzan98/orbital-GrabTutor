package com.example.grabtutor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.grabtutor.Adapter.PaymentHistoryAdapter;
import com.example.grabtutor.Adapter.RewardsHistoryAdapter;
import com.example.grabtutor.Fragment.MessagesFragment;
import com.example.grabtutor.Fragment.ProfileFragment;
import com.example.grabtutor.Model.PaymentHistory;
import com.example.grabtutor.Model.RewardsRedemptionHistory;
import com.example.grabtutor.Model.User;
import com.example.grabtutor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RewardsHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    RewardsHistoryAdapter rewardsHistoryAdapter;
    ArrayList<RewardsRedemptionHistory> list;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_history);

        recyclerView = findViewById(R.id.reward_history_recycler_view);
        back = findViewById(R.id.reward_history_back);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("RewardsHistory").child(firebaseUser.getUid());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RewardsHistoryActivity.this, Rewards.class));
            }
        });



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    RewardsRedemptionHistory rewardsRedemptionHistory = ds.getValue(RewardsRedemptionHistory.class);
                    list.add(rewardsRedemptionHistory);
                }
                //paymentHistoryAdapter.notifyDataSetChanged();
                rewardsHistoryAdapter= new RewardsHistoryAdapter(RewardsHistoryActivity.this, list);
                recyclerView.setAdapter(rewardsHistoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}