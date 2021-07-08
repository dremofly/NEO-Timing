package com.example.neotimingtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
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

import io.neow3j.types.Hash160;

public class activity_ExchangePage extends AppCompatActivity {

    private Button button1000;
    private Button button2000;
    private AlertDialog alert;
    private TextView pointView;
    private String number;
    private int[] iconArray = {R.id.imageView11, R.id.imageView12, R.id.imageView15, R.id.imageView13};
    private int[] textArray = {R.id.textView26, R.id.textView28, R.id.textView27, R.id.textView29};
    private int[] buttonArray = {R.id.button3,R.id.button4,R.id.button5,R.id.button6};
    private Token token;
    private Button homeButton;
    private TextView view36;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_page);
        view36 = findViewById(R.id.textView36);
        view36.setText("Account Address:"+Application.getAccount().getECKeyPair().getAddress());

        pointView = findViewById(R.id.exchangePagePointTextView);
        try {
            pointView.setText("Total Points:" + Application.pointsOf(Application.getAccount().getScriptHash()));
            showOnSaleItem();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        button1000 = findViewById(R.id.button1000);
        button1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(Application.pointsOf(Application.getAccount().getScriptHash()).intValue() < 100) {
                        Toast.makeText(activity_ExchangePage.this, "You have no sufficient points", Toast.LENGTH_SHORT).show();
                    }else {
                        number = button1000.getText().toString();
                        alert.show();
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }


            }
        });

        button2000 = findViewById(R.id.button2000);
        button2000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(Application.pointsOf(Application.getAccount().getScriptHash()).intValue() < 200){
                        Toast.makeText(activity_ExchangePage.this, "You have no sufficient points", Toast.LENGTH_SHORT).show();
                    }else {
                        number = button2000.getText().toString();
                        alert.show();
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        });

        final EditText inputServer = new EditText(this);
        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        alert = new AlertDialog.Builder(this).setTitle("Create a name for your equipment").
                setIcon(R.mipmap.ic_launcher).setView(inputServer).setNegativeButton("cancel", null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = inputServer.getText().toString();

                try {
                    if (number.equals("100")) {
                        Application.withdraw(100, text);
//                        Toast.makeText(activity_ExchangePage.this, "Button100 ======" + text, Toast.LENGTH_SHORT).show();
                    } else {
                        Application.withdraw(200, text);
//                        Toast.makeText(activity_ExchangePage.this, "Button200 ======" + text, Toast.LENGTH_SHORT).show();
                    }

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                Intent intent = new Intent(activity_ExchangePage.this, activity_loadingPage.class);
                intent.putExtra("Name", text);
                startActivity(intent);
            }
        }).create();

        homeButton = findViewById(R.id.buttonHomePage);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(activity_ExchangePage.this, activity_ItemPage.class);
                startActivity(intent3);
            }
        });

    }

    public void showOnSaleItem() throws Throwable {
        ArrayList list = Application.showOnSaleItems();
        int numOfItems = list.size();
        if (numOfItems > 4) {
            numOfItems = 4;
        }
        for (int i = 0; i < numOfItems; i++) {
            TextView view = findViewById(textArray[i]);
            ImageView iView = findViewById(iconArray[i]);
            Button button = findViewById(buttonArray[i]);
            view.setText((CharSequence) list.get(i));
            iView.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int k = contains(buttonArray, button.getId());
                    Log.i("tag", "Number=== " + k);
                    TextView viewt = findViewById(textArray[k]);
                    String text = viewt.getText().toString();

                    Log.i("tag", "Name==== " + text);
                    try {
                        Application.sellOnSaleItem(Application.getAccount().getScriptHash(),text);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    Intent intent = new Intent(activity_ExchangePage.this,activity_afterSale.class);
                    startActivity(intent);


                }
            });

            iView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int k = contains(iconArray, iView.getId());
                    Log.i("tag", "Number=== " + k);
                    TextView view2 = findViewById(textArray[k]);
                    String text = view2.getText().toString();
                    Log.i("tag", "Name==== " + text);
                    try {
                        token = Application.tokenProperties(text);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    Toast.makeText(activity_ExchangePage.this, "===Name===" + token.getName() + "===Owner===" + token.getOwner() + "===Expiration==="
//                            + token.getExpiration() + "===Genre===" + token.getGenre() + "===IsOnSale===" + token.isOnSale() + "===Hp==="
//                            + token.getHp() + "===Attack===" + token.getAttack() + "===Defense===" + token.getDefense(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private int contains(int[] array, int i) {
        int length = array.length;
        int k = length - 1;
        while (k >= 0) {
            if (array[k] == i) {
                return k;
            }
            k--;
        }
        return 0;
    }
}