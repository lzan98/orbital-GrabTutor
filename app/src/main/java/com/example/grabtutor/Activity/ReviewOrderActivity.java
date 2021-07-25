package com.example.grabtutor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grabtutor.Model.OrderHistory;
import com.example.grabtutor.Model.PaymentHistory;
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

import java.util.Calendar;

public class ReviewOrderActivity extends AppCompatActivity {

    private String postId, userId;
    ImageView image;
    TextView title, desc, price, creditBalance;
    Button paymentButton;
    private Context mContext;
    private Post mPost;
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
        paymentButton = findViewById(R.id.paymentButton);

        FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mPost = snapshot.getValue(Post.class);
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

        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int balance = Integer.parseInt("" + snapshot.child("balance").getValue());
                creditBalance.setText("$" + String.valueOf(String.valueOf(balance)));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        /*leaveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewOrderActivity.this, LeaveReviewActivity.class);
                startActivity(intent);
            }
        });*/

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        new AlertDialog.Builder(ReviewOrderActivity.this)
                                    .setTitle("Payment")
                                    .setMessage("Are you sure?")
                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                    int balance = Integer.parseInt("" + snapshot.child("balance").getValue());
                                                    int postPrice = Integer.parseInt(mPost.getPrice());
                                                    if (balance < postPrice) {
                                                        Toast.makeText(ReviewOrderActivity.this, "Insufficient Balance", Toast.LENGTH_LONG).show();
                                                        finish();
                                                    }
                                                    Integer newBalance = balance - postPrice;
                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("balance").setValue(newBalance);
                                                    String currentTime = Calendar.getInstance().getTime().toString();
                                                    String uNumber = Long.toString(Calendar.getInstance().getTimeInMillis());
                                                    String refNumber = firebaseUser.getUid() + uNumber;
                                                    OrderHistory orderHistory1 = new OrderHistory(refNumber
                                                            , postPrice, "In Progress", mPost.getTitle(), "" + firebaseUser.getUid(), mPost.getPublisher(), currentTime, "Buyer");
                                                    FirebaseDatabase.getInstance().getReference("OrderHistory").child(firebaseUser.getUid()).child(refNumber).setValue(orderHistory1);
                                                    OrderHistory orderHistory2 = new OrderHistory(refNumber
                                                            , postPrice, "In Progress", mPost.getTitle(), "" + firebaseUser.getUid(), mPost.getPublisher(), currentTime, "Seller");
                                                    FirebaseDatabase.getInstance().getReference("OrderHistory").child(mPost.getPublisher()).child(refNumber).setValue(orderHistory2);
                                                    startActivity(new Intent(ReviewOrderActivity.this, OrderHistoryActivity.class));

                                                }

                                                @Override
                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                }
                                            });}}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            }).setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                });
    }
}