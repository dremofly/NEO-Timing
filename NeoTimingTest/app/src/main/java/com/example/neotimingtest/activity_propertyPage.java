package com.example.neotimingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.neotimingtest.sdk.Application;
import com.example.neotimingtest.sdk.Token;

public class activity_propertyPage extends AppCompatActivity {

    private TextView owner;
    private TextView name;
    private TextView expiration;
    private TextView isOnSale;
    private TextView genre;
    private TextView hp;
    private TextView attack;
    private TextView defense;
    private Button itemButton;
    private String name1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_page);
        Log.i("TAG", "success ");
        Intent intent = getIntent();
        name1 = getIntent().getStringExtra("Name");
        Token token = null;
        try {
            token = Application.tokenProperties(name1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        owner = findViewById(R.id.textViewOwner);
        owner.setText("Owner: "+ token.getOwner());
        name = findViewById(R.id.textViewName);
        name.setText("Name: "+token.getName());
        expiration = findViewById(R.id.textViewExpiration);
        expiration.setText("Expiration:" +token.getExpiration());
        isOnSale = findViewById(R.id.textViewIsOnsale);
        isOnSale.setText("IsOnSale:"+token.isOnSale());
        genre = findViewById(R.id.textViewGenre);
        genre.setText("Genre:" +token.getGenre());
        hp = findViewById(R.id.textViewHp);
        hp.setText("Hp: "+token.getHp());
        attack = findViewById(R.id.textViewAttack);
        attack.setText("Attack: "+ token.getAttack());
        defense = findViewById(R.id.textViewDefense);
        defense.setText("Defense: "+token.getDefense());
        itemButton = findViewById(R.id.backToItemButton);
        itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentItem = new Intent(activity_propertyPage.this,activity_ItemPage.class);
                startActivity(intentItem);
            }
        });


    }
}