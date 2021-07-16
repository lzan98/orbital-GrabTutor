package com.example.grabtutor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.grabtutor.Adapter.CredentialsHistoryAdapter;
import com.example.grabtutor.Adapter.PaymentHistoryAdapter;
import com.example.grabtutor.Model.CredentialsModel;
import com.example.grabtutor.Model.PaymentHistory;
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

public class CredentialsHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    CredentialsHistoryAdapter credentialsHistoryAdapter;
    ArrayList<CredentialsModel> list;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials_history);

        recyclerView = findViewById(R.id.credentials_history_recycler_view);
        back = findViewById(R.id.credentials_history_back);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Credentials").child(firebaseUser.getUid());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CredentialsHistoryActivity.this, MainActivity.class));
            }
        });



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    CredentialsModel credentialsModel = ds.getValue(CredentialsModel.class);
                    list.add(credentialsModel);
                }
                //paymentHistoryAdapter.notifyDataSetChanged();
                credentialsHistoryAdapter = new CredentialsHistoryAdapter(CredentialsHistoryActivity.this, list);
                recyclerView.setAdapter(credentialsHistoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}