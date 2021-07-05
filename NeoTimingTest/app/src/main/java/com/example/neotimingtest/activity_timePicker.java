package com.example.neotimingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neotimingtest.sdk.Application;

import java.math.BigInteger;

public class activity_timePicker extends AppCompatActivity {

    private int workTime;
    private Button nftButton;
    private TextView pointView;
    private BigInteger points;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);
        pointView= findViewById(R.id.pointTextView);
        points = BigInteger.ZERO;

        try {
            Toast.makeText(this, "Result: "+ points, Toast.LENGTH_LONG).show();
//            Application.setPoint(200);
            points = Application.pointsOf(Application.getAccount().getScriptHash());

        } catch (Throwable throwable) {
            points = BigInteger.ZERO;
            throwable.printStackTrace();
        }
        pointView.setText("Total Points:"+points.toString());
        SeekBar setTimeSeekBar = findViewById(R.id.seekBar);
        TextView showTimeTextView = findViewById(R.id.showTimeTextView);
        setTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showTimeTextView.setText("Your work time : " +progress +" mins");
                workTime = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button triggerButton = findViewById(R.id.triggerButton);
        triggerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_timePicker.this,activity_workPage.class);
                intent.putExtra("Time",workTime);
                try {
                    // TOFIX
                    Application.trigger(workTime);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                startActivity(intent);
            }
        });

        nftButton = findViewById(R.id.NFT_button);
        nftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_timePicker.this,activity_ItemPage.class);
                startActivity(intent);
            }
        });
    }
}