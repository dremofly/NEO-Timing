package com.example.neotimingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.neotimingtest.sdk.Application;

public class activity_createWallet extends AppCompatActivity {

    private TextView view;
    private  Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        Intent intent = getIntent();
        String wif1 = intent.getStringExtra("WIF");
        view = findViewById(R.id.textView31);
        view.setText(wif1);
        button = findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Application.importWallet(wif1);
                Intent intent1 = new Intent(activity_createWallet.this,activity_timePicker.class);
                startActivity(intent1);
            }
        });

    }
}