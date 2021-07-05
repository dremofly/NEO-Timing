package com.example.neotimingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.neotimingtest.sdk.Application;

public class activity_ItemPage extends AppCompatActivity {

    Button exchange ;

    TextView textView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);
        try {
            textView = findViewById(R.id.itemPagePointTextViw);
            textView.setText("Total Points:" + Application.pointsOf(Application.getAccount().getScriptHash()));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        exchange = findViewById(R.id.itemPageExchangeButton);
        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_ItemPage.this,activity_ExchangePage.class);
                startActivity(intent);
            }
        });
    }
}