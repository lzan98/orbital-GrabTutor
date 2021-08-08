package com.example.grabtutor.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.grabtutor.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class EditReviewActivity extends AppCompatActivity {

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    String postId, userId, reviewText;
    RatingBar ratingBar;
    Button btn, delete;
    EditText reviewEt;
    float prevRating;
    private FirebaseAuth firebaseAuth;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_review);

        getSupportActionBar().setTitle("Review");
        postId = getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("postid", "none");
        btn = findViewById(R.id.reviewSubmit);
        delete = findViewById(R.id.reviewDelete);
        reviewEt = findViewById(R.id.reviewInput);
        userId = getIntent().getStringExtra("userId");
        //if user has written review to this shop, load it
        loadMyReview();
        //load post info
        loadPostInfo();
        prevRating = getIntent().getFloatExtra("rating", 0);
        reviewText = getIntent().getStringExtra("reviewText");

        firebaseAuth = FirebaseAuth.getInstance();
        ratingBar = findViewById(R.id.userRating);
        ratingBar.setRating(prevRating);
        reviewEt.setText(reviewText);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
                finish();
                startActivity(new Intent(EditReviewActivity.this, ReviewActivity.class).putExtra("postId", postId));
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

    }

    private void delete() {
        new AlertDialog.Builder(this)
                .setTitle("Remove Post")
                .setMessage("Are you sure?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ref.child(postId).child("Ratings").child(userId).removeValue();
                        finish();
                        startActivity(new Intent(EditReviewActivity.this, ReviewActivity.class).putExtra("postId", postId));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        }).setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }


    private void loadPostInfo() {
        ref.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String title = "" +snapshot.child("title").getValue();
                String imageurl = "" +snapshot.child("imageurl").getValue();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void loadMyReview() {
        ref.child(postId).child("Ratings").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //my review is avail in this shop
                    //get review details

                    String uid = "" + snapshot.child("id").getValue();
                    String ratings = "" + snapshot.child("ratings").getValue();
                    String review = "" + snapshot.child("review").getValue();
                    String timestamp = "" + snapshot.child("timestamp").getValue();

                    float myRating = Float.parseFloat(ratings);
                    ratingBar.setRating(myRating);
                    reviewEt.setText(review);


                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void inputData() {
        String ratings = "" + ratingBar.getRating();
        String review = reviewEt.getText().toString().trim();

        //for time of review
        String timestamp = "" + System.currentTimeMillis();

        //setupdata in hashmap
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", firebaseAuth.getUid());
        hashMap.put("ratings", "" + ratings);
        hashMap.put("review", "" + review);
        hashMap.put("timestamp", "" + timestamp);
        hashMap.put("postId", postId);

        //put to Database: User > postId > Ratings
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.child(postId).child("Ratings").child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditReviewActivity.this, "Review published successfully...", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(EditReviewActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(0,0);
//    }
