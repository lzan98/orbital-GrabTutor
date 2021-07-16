package com.example.grabtutor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.grabtutor.R;

public class DogeCoinPaymentActivity extends AppCompatActivity {

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doge_coin_payment);

        next = findViewById(R.id.button_dogecoin_next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DogeCoinPaymentActivity.this, PaymentConfirmationActivity.class);
                intent.putExtra("paymentType", "DogeCoin");
                intent.putExtra("currency", "DOGE");
                intent.putExtra("status", "Pending");
                startActivity(intent);
            }
        });
    }
}