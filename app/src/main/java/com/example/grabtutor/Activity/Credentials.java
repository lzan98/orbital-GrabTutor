package com.example.grabtutor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.grabtutor.R;

public class Credentials extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView listView;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);

        listView = findViewById(R.id.ListView_credentials);
        back = findViewById(R.id.button_credentials_back);

        String[] management =  {"Upload Credentials", "History"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Credentials.this, android.R.layout.simple_list_item_1, management);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // position 0 = TopUp
        if (position == 0) {
            Intent intent = new Intent(Credentials.this, UploadCredentialsActivity.class);
            startActivity(intent);
        }
        // position 1 = Earnings
        if (position == 1) {
            Intent intent = new Intent(Credentials.this, CredentialsHistoryActivity.class);
            startActivity(intent);
        }
    }
}