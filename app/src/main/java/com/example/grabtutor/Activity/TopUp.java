package com.example.grabtutor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grabtutor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class TopUp extends AppCompatActivity{

    private EditText topUpAmount;
    private TextView currentBalance;
    private Button back, confirm;
    private Intent prevIntent;
    private DatabaseReference ref;
    private final static Integer MAX_BALANCE = 10000;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        topUpAmount = findViewById(R.id.editText_topup_amt_to_topup);
        currentBalance = findViewById(R.id.textView_topup_current_balance);
        back = findViewById(R.id.button_topup_back);
        confirm = findViewById(R.id.button_topup_confirm);
        prevIntent = getIntent();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopUp.this, Payment.class);
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TopUp.this)
                        .setCancelable(true)
                        .setMessage("Confirm Top-up?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                confirm();
                            }
                        })
                        .create()
                        .show();
            }
        });

        ref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                        currentBalance.setText("$" + snapshot.child("balance").getValue(Integer.class).toString());
                } else {
                    Toast.makeText(TopUp.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(TopUp.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirm() {
        String amount = topUpAmount.getText().toString().trim();
        if (amount.isEmpty()) {
            topUpAmount.setError("Entered amount cannot be empty");
            topUpAmount.requestFocus();
            return;
        }
        if (!amount.isEmpty()) {
            Integer intAmount = Integer.parseInt(amount);
            if (intAmount == 0) {
                topUpAmount.setError("Entered amount must be more than $0");
                topUpAmount.requestFocus();
                return;
            }
        }
        ref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                        Integer initBalance = snapshot.child("balance").getValue(Integer.class);
                        Integer maxTopUp = MAX_BALANCE - initBalance;
                        if (initBalance >= MAX_BALANCE) {
                            Toast.makeText(TopUp.this, "Cannot have more than $10,000 in your account", Toast.LENGTH_SHORT).show();
                            topUpAmount.getText().clear();
                        } if (Integer.parseInt(amount) > maxTopUp) {
                            Toast.makeText(TopUp.this, "Maximum of $" + maxTopUp.toString() + " more allowed for top up", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Integer newBalance = initBalance + Integer.parseInt(amount);
                            ref.child(user.getUid()).child("balance").setValue(newBalance);
                            currentBalance.setText("$" + newBalance.toString());
                            topUpAmount.getText().clear();
                            Toast.makeText(TopUp.this, "$" + amount + " added successfully", Toast.LENGTH_SHORT).show();
                          }
                } else {
                    Toast.makeText(TopUp.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(TopUp.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }


}