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

import org.w3c.dom.Text;

public class activity_importWallet extends AppCompatActivity {

//    public static final String K_INT = "k_int";
//    public static final String K_BOOL = "k_bool";
//    public static final String K_STR = "k_str";
//    public static final String K_TITLE = "k_titile";
//    public static final String K_SUB_TITLE =  "k_sub_title";
    private static final String TAG = "IMPORT WALLET";
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
                EditText wifText = findViewById(R.id.wiftext);
                Log.v(TAG, "input private key wif: " + wifText.getText().toString());
                Toast.makeText(activity_importWallet.this, wifText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
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