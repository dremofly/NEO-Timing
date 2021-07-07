package com.example.neotimingtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neotimingtest.sdk.Application;
import com.example.neotimingtest.sdk.Token;

import java.util.ArrayList;
import java.util.Arrays;

import io.neow3j.types.Hash160;

public class activity_ItemPage extends AppCompatActivity {

    private Button exchange ;


    private TextView textView ;

    private int[] frameArray = {R.id.textView8, R.id.textView10, R.id.textView11, R.id.textView12, R.id.textView13, R.id.textView14};

    private int[] iconArray = {R.id.imageView5,R.id.imageView3,R.id.imageView7,R.id.imageView4,R.id.imageView2,R.id.imageView6};

    private Token token;

    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);
        try {
            textView = findViewById(R.id.itemPagePointTextViw);
            textView.setText("Total Points:" + Application.pointsOf(Application.getAccount().getScriptHash()));
            showItem(Application.getAccount().getScriptHash());
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

    @Override
    protected void onRestart() {
        super.onRestart();
        setContentView(R.layout.activity_item_page);
        try {
            textView = findViewById(R.id.itemPagePointTextViw);
            textView.setText("Total Points:" + Application.pointsOf(Application.getAccount().getScriptHash()));
            showItem(Application.getAccount().getScriptHash());
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

    public void showItem(Hash160 address) throws Exception {
        ArrayList list = Application.showMyItems(Application.getAccount().getScriptHash());
        int numOfItems = list.size();
        if(numOfItems > 6) {
            numOfItems = 6;
        }
        for (int i = 0; i < numOfItems; i ++) {
            TextView view = findViewById(frameArray[i]);
            ImageView iView = findViewById(iconArray[i]);
            view.setText((CharSequence) list.get(i));
            iView.setVisibility(View.VISIBLE);
            iView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int k = contains(iconArray,iView.getId());
                    Log.i("tag", "Number=== "+k);
                    TextView view2 = findViewById(frameArray[k]);
                    String text = view2.getText().toString();
                    Log.i("tag", "Name==== "+text);
                    try {
                        token = Application.tokenProperties(text);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(activity_ItemPage.this, "===Name==="+token.getName()+"===Owner==="+token.getOwner()+"===Expiration==="
                            +token.getExpiration()+"===Genre==="+token.getGenre()+"===IsOnSale==="+token.isOnSale()+"===Hp==="
                            +token.getHp()+"===Attack==="+token.getAttack()+"===Defense==="+token.getDefense(), Toast.LENGTH_SHORT).show();
//                    alertDialog.show();
                    Intent intent = new Intent(activity_ItemPage.this,activity_propertyPage1.class);
                    intent.putExtra("Property",text);
                    startActivity(intent);
                }
            });
        }
    }

    private int contains(int[] array, int i){
        int length = array.length;
        int k = length - 1;
        while( k  >= 0) {
            if(array[k] == i) {
                return k;
            }
            k --;
        }
        return 0;
    }


//    AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("Your NFT properties").
//            setIcon(R.mipmap.ic_launcher).
//            setMessage("===Name==="+token.getName()+"===Owner==="+token.getOwner()+"===Expiration==="
//                    +token.getExpiration()+"===Genre==="+token.getGenre()+"===IsOnSale==="+token.isOnSale()+"===Hp==="
//                    +token.getHp()+"===Attack==="+token.getAttack()+"===Defense==="+token.getDefense())
//            .setNeutralButton("Ok",null).create();
}