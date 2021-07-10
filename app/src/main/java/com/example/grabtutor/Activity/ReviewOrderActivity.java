package com.example.grabtutor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grabtutor.Model.Post;
import com.example.grabtutor.Model.User;
import com.example.grabtutor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ReviewOrderActivity extends AppCompatActivity {

    private String postId, userId;
    ImageView image;
    TextView title, desc, price, creditBalance;
    Button leaveReview;
    private Context mContext;
    private Post mPost;
    private Button paymentButton;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_order);
        postId = getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("postid", "none");
        userId = getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("id", "none");
        image = findViewById(R.id.post_image);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.orderDetails);
        price = findViewById(R.id.price);
        creditBalance = findViewById(R.id.creditBalance);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        leaveReview = findViewById(R.id.leaveReview);

        FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                Picasso.get().load(post.getImageurl()).into(image);
                title.setText(post.getTitle());
                desc.setText(post.getDescription());
                price.setText("$" + post.getPrice());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                int test = user.getBalance();
                creditBalance.setText(String.valueOf(test));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        leaveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewOrderActivity.this, LeaveReviewActivity.class);
                startActivity(intent);
            }
        });
    }
}