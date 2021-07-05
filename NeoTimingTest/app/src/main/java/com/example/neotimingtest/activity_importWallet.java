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

//    public static final String K_INT = "k_int";
//    public static final String K_BOOL = "k_bool";
//    public static final String K_STR = "k_str";
//    public static final String K_TITLE = "k_titile";
//    public static final String K_SUB_TITLE =  "k_sub_title";
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
                //Intent intent = new Intent(activity_createWallet.this, activity_createWallet.class);
                //startActivity(intent);
                wifText = findViewById(R.id.wiftext);
                wif = wifText.getText().toString();
                Log.v(TAG, "input private key wif: " + wif);
                Toast.makeText(activity_importWallet.this, wif, Toast.LENGTH_SHORT).show();
                Application.startConnection();
                Application.importWallet(wif);
                Application.getAccount();

                Intent intent = new Intent(activity_importWallet.this,activity_timePicker.class);
                startActivity(intent);
//                try {
//                    Log.i("Tag", "Result: ============="+ String.valueOf(Application.pointsOf(Application.account.getScriptHash())));
//
//                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
//                }
            }
        });
    }


    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

//    private  void gotInput(){
//        Intent intent = getIntent();
//        if (intent != null) {
//            String str = intent.getStringExtra(K_STR);
//            TextView tv2 = findViewById(R.id.editTextTextPersonName2);
//            tv2.setText(str);
//            setContentView(R.layout.activity_display);
//
//            Intent resultIntent = new Intent();
//            TextView tv3 = findViewById(R.id.editTextTextPersonName5);
//            TextView tv4 = findViewById(R.id.editTextTextPersonName6);
//            resultIntent.putExtra(K_TITLE,tv3.getText().toString());
//            resultIntent.putExtra(K_SUB_TITLE,tv4.getText().toString());
//            setResult(RESULT_OK, resultIntent);
//            Log.d("TAG","output:"+ str );
//        } else {
//            Log.d("TAG","output:error");
//        }
//    }
}