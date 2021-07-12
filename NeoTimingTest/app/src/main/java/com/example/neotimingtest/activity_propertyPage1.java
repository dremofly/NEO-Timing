package com.example.neotimingtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neotimingtest.sdk.Application;
import com.example.neotimingtest.sdk.Token;

import org.w3c.dom.Text;

public class activity_propertyPage1 extends AppCompatActivity {

    private Button sell;
    private AlertDialog alert;
    private Token token;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_page1);
        Intent intent = getIntent();
        String s = intent.getStringExtra("Property");
        try {
           token = Application.tokenProperties(s);
//           Toast.makeText(this, "Name==="+token.getName(), Toast.LENGTH_SHORT).show();
           TextView view18 = findViewById(R.id.textView18);
           view18.setText("Name======  "+token.getName());
           TextView view19 = findViewById(R.id.textView19);
           view19.setText("Owner======  " +token.getOwner());
           TextView view20 = findViewById(R.id.textView20);
           view20.setText("Expiration=====  "+token.getExpiration());
           TextView view21 = findViewById(R.id.textView21);
           view21.setText("IsOnsale=====  " +token.isOnSale());
           TextView view22 = findViewById(R.id.textView22);
           view22.setText("Genre=====  "+token.getGenre());
           TextView view23 = findViewById(R.id.textView23);
           view23.setText("Hp=====  "+token.getHp());
           TextView view24 = findViewById(R.id.textView24);
           view24.setText("Attack=====  "+token.getAttack());
           TextView view25 = findViewById(R.id.textView25);
           view25.setText("Defense=====  "+token.getDefense());
        } catch (Exception e) {
            e.printStackTrace();
        }
        back = findViewById(R.id.button9);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_propertyPage1.this,activity_ItemPage.class);
                startActivity(intent);
            }
        });


        sell = findViewById(R.id.button);
        if(saleState()){
            sell.setText("Cancel Sell");
        }
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saleState() == false){
                    alert.show();
                }else {
                    try {
                        Application.cancelOnSale(token.getOwner(),token.getName());
                        Intent intent = new Intent(activity_propertyPage1.this,activity_ExchangePage.class);
                        startActivity(intent);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        });

        final EditText input = new EditText(this);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        alert = new AlertDialog.Builder(this).setTitle("Set a price for your NFT! /Neo").
                setIcon(R.mipmap.ic_launcher).setView(input).setNegativeButton("cancel",null).setPositiveButton("Sell", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                try {
                    Application.onSale(token.getOwner(),token.getName());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Intent intent = new Intent(activity_propertyPage1.this,activity_ItemPage.class);
                intent.putExtra("Price",text);
                startActivity(intent);
            }
        }).create();
    }

    private boolean saleState(){
        if(token.isOnSale() == true) {
            return true;
        }else {
            return false;
        }
    }
}