package com.example.neotimingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class activity_instructionPage extends AppCompatActivity {

    private TextView title ;
    private int time ;
    private TextView instruction;
    private Button homepageButton ;
    private Button NftButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_page);

        Intent intent = getIntent();
        time = intent.getIntExtra("Time",0);
        instruction =  findViewById(R.id.InstructionTextView);
        instruction.setText("Congragulations! You managed a " + time + " minutes' working task right now. And you earned " + time * 60 + " points which you can exchange for your pet equipments! Go check your properties in your NFT Storage." );
        homepageButton = findViewById(R.id.backtoWorkPageButton);
        homepageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(activity_instructionPage.this,activity_timePicker.class);
                startActivity(intent);
            }
        });
        NftButton = findViewById(R.id.backToItemButton);
        NftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity_instructionPage.this,activity_ItemPage.class);

                startActivity(intent);
            }
        });
    }
}