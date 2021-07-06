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

public class activity_ExchangePage extends AppCompatActivity {
    Button button1000 ;
    Button button2000;
    private AlertDialog alert;
    private TextView pointView;
    private String number;

    private Button homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_page);
        pointView = findViewById(R.id.exchangePagePointTextView);
        try {
            pointView.setText("Total Points:" + Application.pointsOf(Application.getAccount().getScriptHash()));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        button1000 = findViewById(R.id.button1000);

        button1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                   if(Application.pointsOf(Application.getAccount().getScriptHash()).intValue() < 1000){
//                       Toast.makeText(activity_ExchangePage.this, "You don't have enough points.", Toast.LENGTH_SHORT).show();
//                   }
//                   else {
//                       alert.show();
//                   }
//                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
//                }

                number = button1000.getText().toString();
                alert.show();

            }
        });
        button2000 = findViewById(R.id.button2000);
        button2000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number =button2000.getText().toString();
                alert.show();
            }
        });
        final EditText inputServer = new EditText(this);
        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        alert = new AlertDialog.Builder(this).setTitle("Create a name for your equipment").
                setIcon(R.mipmap.ic_launcher).setView(inputServer).setNegativeButton("cancel",null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = inputServer.getText().toString();

                try {
                    if(number.equals("1000")){
                        Application.withdraw(10,text);
                        Toast.makeText(activity_ExchangePage.this, "Button1000 ======"+text, Toast.LENGTH_SHORT).show();
                    }else{
                        Application.withdraw(20,text);
                        Toast.makeText(activity_ExchangePage.this, "Button2000 ======"+text, Toast.LENGTH_SHORT).show();
                    }

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Intent intent = new Intent(activity_ExchangePage.this,activity_loadingPage.class);
                intent.putExtra("Name",text);
                startActivity(intent);
            }
        }).create();

        homeButton = findViewById(R.id.buttonHomePage);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(activity_ExchangePage.this,activity_timePicker.class);
                startActivity(intent3);
            }
        });

    }
}