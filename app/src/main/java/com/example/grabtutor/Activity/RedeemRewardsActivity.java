package com.example.grabtutor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grabtutor.Model.RewardsRedemptionHistory;
import com.example.grabtutor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class RedeemRewardsActivity extends AppCompatActivity {

    TextView title, points, available;
    ImageView picture, back;
    Button redeem;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_rewards);

        title = findViewById(R.id.redeem_rewards_title);
        points = findViewById(R.id.redeem_rewards_points);
        available = findViewById(R.id.redeem_rewards_points_available);
        picture = findViewById(R.id.redeem_rewards_picture);
        back = findViewById(R.id.redeem_rewards_back);
        redeem = findViewById(R.id.redeem_rewards_confirm);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        String title_db = intent.getStringExtra("title");
        String points_db = intent.getStringExtra("points");
        String picture_db = intent.getStringExtra("picture");

        title.setText(title_db);
        points.setText(points_db + " Points to redeem");
        Picasso.get().load(picture_db).into(picture);

        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Integer userPoints = snapshot.child("points").getValue(Integer.class);
                available.setText(userPoints.toString());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RedeemRewardsActivity.this, Rewards.class));
            }
        });

        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(RedeemRewardsActivity.this)
                        .setCancelable(true)
                        .setMessage("Confirm Redeem?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Redeem();
                            }
                        })
                        .create()
                        .show();
            }
        });

    }

    private void Redeem() {
        Integer rewardPoints = Integer.parseInt(getIntent().getStringExtra("points"));
        Intent intent = getIntent();
        String title_db = intent.getStringExtra("title");
        String points_db = intent.getStringExtra("points");
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Integer userPoints = snapshot.child("points").getValue(Integer.class);

                    if (userPoints < rewardPoints) {
                        Toast.makeText(RedeemRewardsActivity.this, "Insufficient points", Toast.LENGTH_SHORT).show();
                    } else {
                        Integer difference = userPoints - rewardPoints;
                        databaseReference.child(firebaseUser.getUid()).child("points").setValue(difference);
                        Toast.makeText(RedeemRewardsActivity.this, "Reward redeemed successfully", Toast.LENGTH_SHORT).show();
                        String currentTime = Calendar.getInstance().getTime().toString();
                        String uNumber = firebaseUser.getUid() + Long.toString(Calendar.getInstance().getTimeInMillis());
                        FirebaseDatabase.getInstance().getReference("RewardsHistory").child(firebaseUser.getUid()).child(uNumber)
                                .setValue(new RewardsRedemptionHistory(uNumber, title_db, points_db, "Pending", currentTime));

                        finish();
                        startActivity(new Intent(RedeemRewardsActivity.this, MainActivity.class));
                    }
                } else {
                    Toast.makeText(RedeemRewardsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(RedeemRewardsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}