package com.example.neotimingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class activity_timePicker extends AppCompatActivity {

    private int workTime;
    private Button nftButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);
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