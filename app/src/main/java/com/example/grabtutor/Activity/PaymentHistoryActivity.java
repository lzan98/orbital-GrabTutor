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
import com.example.grabtutor.Fragment.MessagesFragment;
import com.example.grabtutor.Fragment.ProfileFragment;
import com.example.grabtutor.Model.PaymentHistory;
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

public class PaymentHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    PaymentHistoryAdapter paymentHistoryAdapter;
    ArrayList<PaymentHistory> list;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        recyclerView = findViewById(R.id.paymenthistory_recycler_view);
        back = findViewById(R.id.payment_history_back);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("PaymentHistory").child(firebaseUser.getUid());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentHistoryActivity.this, Payment.class));
            }
        });



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    PaymentHistory paymentHistory = ds.getValue(PaymentHistory.class);
                    list.add(paymentHistory);
                }
                //paymentHistoryAdapter.notifyDataSetChanged();
                paymentHistoryAdapter = new PaymentHistoryAdapter(PaymentHistoryActivity.this, list);
                recyclerView.setAdapter(paymentHistoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}