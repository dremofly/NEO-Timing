package com.example.neotimingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neotimingtest.sdk.Application;

import org.w3c.dom.Text;

import io.neow3j.protocol.Neow3j;
import io.neow3j.wallet.Account;
import io.neow3j.wallet.Wallet;

public class activity_importWallet extends AppCompatActivity {

    private static final String TAG = "IMPORT WALLET";
    private EditText wifText;
    private String wif;
    private Neow3j neow3j;
    private Wallet wallet;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_wallet);
        Button uploadPrivateKeyButton = findViewById(R.id.upload_private_key_button);
        uploadPrivateKeyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                wifText = findViewById(R.id.wiftext);
                wif = wifText.getText().toString();
                Log.v(TAG, "input private key wif: " + wif);
//                Toast.makeText(activity_importWallet.this, wif, Toast.LENGTH_SHORT).show();
                Application.startConnection();
                Application.importWallet(wif);
                Application.getAccount();
                Intent intent = new Intent(activity_importWallet.this,activity_timePicker.class);
                startActivity(intent);

            }
        });
    }
}