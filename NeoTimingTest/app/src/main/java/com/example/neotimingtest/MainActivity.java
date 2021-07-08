package com.example.neotimingtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

    private String wif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //give permission to socket in main thread.
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        //Press the creat Button, start the connection with neo chain,and create a default account
        Button creatWalletButton = findViewById(R.id.create_wallet_button);
        creatWalletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Application.startConnection();
                    wif = Application.createAccount();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Intent intent = new Intent(MainActivity.this, activity_createWallet.class);
                intent.putExtra("WIF",wif);
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
    public void finish() {
        super.finish();
        AlertDialog.Builder dialog = new AlertDialog.Builder(this).setTitle("Reminder").
                setMessage("If you quit the app, you can not get any reward, are you sure to exit?").
                setIcon(R.mipmap.neo_logo).
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Application.cancel();
                            System.exit(0);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }).
                setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        if (dialog != null) {
            dialog.show();
        }
    }

}