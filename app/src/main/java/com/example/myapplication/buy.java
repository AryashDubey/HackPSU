package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pl.droidsonroids.gif.GifImageView;

public class buy extends AppCompatActivity {

    Button signin;
    Button signup;
    GifImageView Gif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        Gif = findViewById(R.id.imageView3);
        String uri = "@drawable/cbbb";
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Gif.setImageResource(imageResource);
        signup= findViewById(R.id.button);
        signup.setOnClickListener(view -> {
            Intent intent= new Intent(getApplicationContext(),register.class);
            startActivity(intent);
        });
        signin= findViewById(R.id.button4);
        signin.setOnClickListener(view -> {
            Intent intent= new Intent(getApplicationContext(),login.class);
            startActivity(intent);
        });
    }
}