package com.example.grabtutor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grabtutor.Adapter.OrderHistoryAdapter;
import com.example.grabtutor.Model.OrderHistory;
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

public class ConfirmCompletionActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    String refNumber, publisherId;
    int postPrice;
    FirebaseUser firebaseUser;
    OrderHistoryAdapter orderHistoryAdapter;
    ArrayList<OrderHistory> list;
    ImageView back;

    TextView ref, price, postTitle, status, tutor, date, type;
    Button reviewButton, completedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_completion);

        refNumber = getIntent().getStringExtra("refNumber");
        publisherId = getIntent().getStringExtra("publisherId");
        postPrice = getIntent().getIntExtra("price", 0);
        back = findViewById(R.id.order_history_back);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("OrderHistory").child(firebaseUser.getUid()).child(refNumber);

        ref = findViewById(R.id.order_history_item_ref);
        price = findViewById(R.id.order_history_item_price);
        status = findViewById(R.id.order_history_item_status);
        postTitle = findViewById(R.id.order_history_item_postTitle);
        tutor = findViewById(R.id.order_history_item_tutor);
        date = findViewById(R.id.order_history_item_date);
        type = findViewById(R.id.order_history_item_type);
        reviewButton = findViewById(R.id.leaveReview);
        completedButton = findViewById(R.id.confirm);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                FirebaseDatabase.getInstance().getReference("Users").child(publisherId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        String username = "" + snapshot.child("username").getValue();
                        tutor.setText(username);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

                String refNumber = "" + snapshot.child("refNumber").getValue();
                String orderTitle = "" + snapshot.child("postTitle").getValue();
                String orderPrice = "" + snapshot.child("postPrice").getValue();
                String orderDate = "" + snapshot.child("date").getValue();
                String orderStatus = "" + snapshot.child("status").getValue();
                String orderType = "" + snapshot.child("type").getValue();

                ref.setText(refNumber);
                price.setText(orderPrice);
                postTitle.setText(orderTitle);
                date.setText(orderDate);
                status.setText(orderStatus);
                type.setText(orderType);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        completedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("OrderHistory").child(firebaseUser.getUid()).child(refNumber).child("status").setValue("Completed");
                FirebaseDatabase.getInstance().getReference("OrderHistory").child(publisherId).child(refNumber).child("status").setValue("Completed");


                FirebaseDatabase.getInstance().getReference("Users").child(publisherId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        Integer initBalance = snapshot.child("balance").getValue(Integer.class);
                        Integer newBalance = initBalance + postPrice;
                        FirebaseDatabase.getInstance().getReference("Users").child(publisherId).child("balance").setValue(newBalance);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmCompletionActivity.this, LeaveReviewActivity.class);
                startActivity(intent);
            }
        });
    }
}