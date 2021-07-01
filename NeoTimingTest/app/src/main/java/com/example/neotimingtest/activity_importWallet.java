package com.example.neotimingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class activity_importWallet extends AppCompatActivity {

//    public static final String K_INT = "k_int";
//    public static final String K_BOOL = "k_bool";
//    public static final String K_STR = "k_str";
//    public static final String K_TITLE = "k_titile";
//    public static final String K_SUB_TITLE =  "k_sub_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_wallet);
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