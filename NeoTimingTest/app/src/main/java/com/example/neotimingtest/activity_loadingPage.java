package com.example.neotimingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neotimingtest.sdk.Application;
import com.example.neotimingtest.sdk.Token;

import java.math.BigInteger;

public class activity_loadingPage extends AppCompatActivity {

    private Button openButton;
    private String name;
    private Token token;
    private TextView nameView;
    private TextView ownerView;
    private TextView genreView;
    private TextView expirationView;
    private TextView isOnSaleView;
    private TextView hpView;
    private TextView attackView;
    private TextView defenseView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);

        openButton = findViewById(R.id.buttonOpen);
        openButton.setEnabled(false);
        backButton = findViewById(R.id.button_back);

        Intent intent = getIntent();
        name = intent.getStringExtra("Name");
        CountDownTimer counter = new CountDownTimer(30 * 1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                openButton.setText("Open "+millisUntilFinished/1000+" s");
            }

            @Override
            public void onFinish() {
                openButton.setText("Open ");
                openButton.setEnabled(true);
                backButton.setVisibility(View.VISIBLE);

            }
        };
        counter.start();
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    token = Application.tokenProperties(name);
                    Toast.makeText(activity_loadingPage.this, "Success", Toast.LENGTH_LONG).show();

                    nameView = findViewById(R.id.textView6);
                    nameView.setText("Name==="+token.getName());

                    ownerView = findViewById(R.id.textView3);
                    ownerView.setText("Owner==="+token.getOwner().toString());

                    genreView = findViewById(R.id.textView5);
                    genreView.setText("Genre==="+token.getGenre());

                    expirationView = findViewById(R.id.textView4);
                    expirationView.setText("Expiration==="+token.getExpiration());

                    isOnSaleView = findViewById(R.id.textView16);
                    isOnSaleView.setText("IsOnSale==="+token.isOnSale());

                    hpView = findViewById(R.id.textView9);
                    hpView.setText("Hp==="+token.getHp());

                    attackView = findViewById(R.id.textView17);
                    attackView.setText("Attack==="+token.getAttack());

                    defenseView = findViewById(R.id.textView7);
                    defenseView.setText("Defense==="+token.getDefense());

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity_loadingPage.this, "Withdraw Error", Toast.LENGTH_LONG).show();
                    Intent intent2 = new Intent(activity_loadingPage.this,activity_ExchangePage.class);
                    startActivity(intent2);
                }
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(activity_loadingPage.this,activity_ExchangePage.class);
                startActivity(intent1);
            }
        });
    }
}