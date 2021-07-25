package com.example.grabtutor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.grabtutor.R;

public class PaymentMethodActivity extends AppCompatActivity {

    Button bank, crypto, free_topup, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        bank = findViewById(R.id.button_paymentmethod_bank);
        crypto = findViewById(R.id.button_paymentmethod_crypto);
        free_topup = findViewById(R.id.button_paymentmethod_free);
        back = findViewById(R.id.button_paymentmethod_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentMethodActivity.this, Payment.class));
            }
        });

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentMethodActivity.this, BankPaymentActivity.class));
            }
        });

        crypto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentMethodActivity.this, CryptoPaymentActivity.class));
            }
        });

        free_topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentMethodActivity.this, TopUp.class));
            }
        });

    }
}