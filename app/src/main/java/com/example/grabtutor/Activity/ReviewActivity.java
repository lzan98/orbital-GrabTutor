package com.example.grabtutor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.grabtutor.Adapter.ReviewAdapter;
import com.example.grabtutor.Model.Review;
import com.example.grabtutor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {
    private String postId;

    private ImageButton backBtn;
    private TextView username, ratingsTv;
    private RatingBar ratingBar;
    private RecyclerView reviewsRv;

    private FirebaseAuth firebaseAuth;

    private ArrayList<Review> reviewArrayList; //list of reviews
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        //backBtn = findViewById(R.id.backBtn);

        ratingsTv = findViewById(R.id.ratingsTv);
        ratingBar = findViewById(R.id.ratingBar);
        reviewsRv = findViewById(R.id.reviewRV);


        postId = getIntent().getStringExtra("postId");

        firebaseAuth = FirebaseAuth.getInstance();
        loadPostDetails();
        loadReviews();

    }

    private float ratingSum = 0;
    private void loadReviews() {
        reviewArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.child(postId).child("Ratings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                reviewArrayList.clear();
                ratingSum = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    float rating = Float.parseFloat("" + ds.child("ratings").getValue());
                    ratingSum = ratingSum + rating;

                    Review review = ds.getValue(Review.class);
                    reviewArrayList.add(review);
                }

                reviewAdapter = new ReviewAdapter(ReviewActivity.this, reviewArrayList);
                reviewsRv.setAdapter(reviewAdapter);

                long numberOfReviews = snapshot.getChildrenCount();
                float avgRating = ratingSum/numberOfReviews;
                ratingsTv.setText(String.format("%.1f", avgRating) + "(" + numberOfReviews + ")");
                ratingBar.setRating(avgRating);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void loadPostDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String publisher = "" + snapshot.child("publisher").getValue();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}