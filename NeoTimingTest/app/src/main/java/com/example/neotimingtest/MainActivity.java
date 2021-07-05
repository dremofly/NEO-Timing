package com.example.neotimingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.neotimingtest.sdk.Application;

import java.io.IOException;
import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

    static final int REQ_CODE = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
//

        Button creatWalletButton = findViewById(R.id.create_wallet_button);
        creatWalletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_timePicker.class);
                startActivity(intent);
            }
        });
        Button importWalletButton = findViewById(R.id.import_wallet_button);
        importWalletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, activity_importWallet.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE:
                if(resultCode== RESULT_OK) {
                    if(data != null)
                    {
                        Toast.makeText(getApplicationContext(), "未保存修改", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }


}