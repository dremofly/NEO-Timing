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
//        neow3j = Neow3j.build(new HttpService("http://127.0.0.1:50012"));

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
                Application.startConnection();
                Application.importWallet(Application.privateKeyWif,"Neo");
                try {
                    Log.i("Tag", "Result: ============="+ String.valueOf(Application.pointsOf(Application.account.getScriptHash())));

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
//                Application.neow3j.catchUpToLatestAndSubscribeToNewBlocksObservable(new BigInteger("100"), true)
//                        .subscribe((blockReqResult) -> {
//                            Log.i("TAG", "onClick: "+"blockIndex: " + blockReqResult.getBlock().getIndex());
//                            Log.i("TAG","hashId: " + blockReqResult.getBlock().getHash());
//                            Log.i("TAG","confirmations: " + blockReqResult.getBlock().getConfirmations());
//                            Log.i("TAG","transactions: " + blockReqResult.getBlock().getTransactions());
//                        });

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