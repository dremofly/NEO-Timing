package com.example.neotimingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;

import com.example.neotimingtest.sdk.Application;

public class activity_afterSale extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sale);

        button = findViewById(R.id.button2);
        button.setEnabled(false);

        CountDownTimer timer = new CountDownTimer(20 * 1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                button.setText("My NFTs:" + millisUntilFinished / 1000 + " s");
            }

            @Override
            public void onFinish() {
                button.setEnabled(true);
                Intent intent = new Intent(activity_afterSale.this,activity_ItemPage.class);
                startActivity(intent);
            }
        };
        timer.start();
    }
}