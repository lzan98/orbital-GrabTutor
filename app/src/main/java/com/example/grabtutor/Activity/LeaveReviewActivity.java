package com.example.grabtutor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.grabtutor.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class LeaveReviewActivity extends AppCompatActivity {

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    String postId, userId;
    RatingBar ratingBar;
    Button btn;
    EditText reviewEt;
    String locName;
    int userRating;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_review);

        getSupportActionBar().setTitle("Review");
        postId = getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("postid", "none");
        btn = findViewById(R.id.reviewSubmit);
        reviewEt = findViewById(R.id.reviewInput);
        userId = getSharedPreferences("PREFS",Context.MODE_PRIVATE).getString("id", "none");
        //if user has written review to this shop, load it
        loadMyReview();
        //load post info
        loadPostInfo();

        firebaseAuth = FirebaseAuth.getInstance();
        ratingBar = findViewById(R.id.userRating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                userRating = (int) rating;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
    }

    private void loadPostInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
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

        //put to Database: User > postId > Ratings
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.child(postId).child("Ratings").child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(LeaveReviewActivity.this, "Review published successfully...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(LeaveReviewActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(0,0);
//    }
