package com.example.neotimingtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neotimingtest.sdk.Application;

public class activity_workPage extends AppCompatActivity {

    private Button cancel_button;
    private TextView timeView ;
    private AlertDialog alertDialog;
    private int time;
    private int count = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_page);
        cancel_button = findViewById(R.id.cancelButton);
        cancel_button.setEnabled(false);
        timeView = findViewById(R.id.timeTextView);

        Intent intent = getIntent();
        time = intent.getIntExtra("Time",0);
        CountDownTimer timer = new CountDownTimer(time * 1000 * 60,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeView.setText("Your working time:" + millisUntilFinished / 1000 + " seconds");
                count --;
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(activity_workPage.this,activity_instructionPage.class);
                intent.putExtra("Time",time);
                try {
                    Application.end();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                startActivity(intent);
            }
        };
        timer.start();
        CountDownTimer timer1 = new CountDownTimer(20 *1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                cancel_button.setText("cancel "+millisUntilFinished/1000+" s");
            }

            @Override
            public void onFinish() {
                cancel_button.setEnabled(true);
                cancel_button.setText("cancel");
            }
        };
        timer1.start();


        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.show();
            }
        });

         alertDialog = new AlertDialog.Builder(this).setTitle("Instruction").
                setMessage("If you cancel this work task now , you will not get any point reward. Are you sure to stop?").
                setIcon(R.mipmap.ic_launcher).
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent1 = new Intent(activity_workPage.this,activity_timePicker.class);
//                        startActivity(intent1);
                        try {
                            Application.cancel();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                        finish();
                    }
                }).
                setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();

    }
}