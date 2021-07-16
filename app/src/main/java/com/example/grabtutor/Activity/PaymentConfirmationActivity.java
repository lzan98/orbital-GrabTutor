package com.example.grabtutor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grabtutor.Model.PaymentHistory;
import com.example.grabtutor.Model.User;
import com.example.grabtutor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;
import java.util.Date;

public class PaymentConfirmationActivity extends AppCompatActivity {

    Button confirm, cancel;
    FirebaseAuth mAuth;
    DatabaseReference ref;
    EditText depositAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);

        confirm = findViewById(R.id.button_confirmpage_confirm);
        cancel = findViewById(R.id.button_confirmpage_cancel);
        mAuth = FirebaseAuth.getInstance();
        depositAmt = findViewById(R.id.editText_paymentconfirm_depositamt);
        ref = FirebaseDatabase.getInstance().getReference("PaymentHistory");
        String currentTime = Calendar.getInstance().getTime().toString();
        String uNumber = Long.toString(Calendar.getInstance().getTimeInMillis());


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (depositAmt.getText().toString().trim().isEmpty()) {
                    depositAmt.setError("Field cannot be empty");
                    depositAmt.requestFocus();
                } else {
                    String refNumber = mAuth.getCurrentUser().getUid() + uNumber;
                    Intent intent = getIntent();
                    String type = intent.getStringExtra("paymentType");
                    String currency = intent.getStringExtra("currency");
                    String status = intent.getStringExtra("status");

                    PaymentHistory paymentHistory = new PaymentHistory(refNumber
                            , depositAmt.getText().toString().trim() + " " + currency, status, type, currentTime);
                    ref.child(mAuth.getCurrentUser().getUid()).child(refNumber).setValue(paymentHistory);
                    startActivity(new Intent(PaymentConfirmationActivity.this, MainActivity.class));
                    finish();
                    Toast.makeText(PaymentConfirmationActivity.this, "Thank you for your confirmation, you can check your transfer status in the payment history", Toast.LENGTH_SHORT).show();


                }
              }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentConfirmationActivity.this, MainActivity.class));
            }
        });
    }
}