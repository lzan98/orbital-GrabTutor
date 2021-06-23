package com.example.grabtutor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.grabtutor.R;

public class Payment extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        listView = findViewById(R.id.ListView_payment);
        back = findViewById(R.id.button_payment_back);

        String[] management =  {"Top up", "Earnings"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Payment.this, android.R.layout.simple_list_item_1, management);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // position 0 = TopUp
        if (position == 0) {
           Intent intent = new Intent(Payment.this, TopUp.class);
           startActivity(intent);
        }
        // position 1 = Earnings
        if (position == 1) {
            Toast.makeText(this, "Work in Progress", Toast.LENGTH_SHORT).show();
        }
    }
}