package com.example.grabtutor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.grabtutor.R;

public class CryptoPaymentActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_payment);

        listView = findViewById(R.id.ListView_cryptotransfer);

        String[] management =  {"BitCoin", "DogeCoin"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(CryptoPaymentActivity.this, android.R.layout.simple_list_item_1, management);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // position 0 = TopUp
        if (position == 0) {
            Intent intent = new Intent(CryptoPaymentActivity.this, BitCoinPaymentActivity.class);
            startActivity(intent);
        }
        // position 1 = Earnings
        if (position == 1) {
            Intent intent = new Intent(CryptoPaymentActivity.this, DogeCoinPaymentActivity.class);
            startActivity(intent);
        }
    }
}