package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    static String mode="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Button sell = findViewById(R.id.button3);
        Button buy = findViewById(R.id.button10);
        sell.setOnClickListener(view -> {
            if(mAuth.getCurrentUser()!=null){
                Intent intent = new Intent(getApplicationContext(),Upload.class);
                startActivity(intent);
            }
            else {
                mode = "sell";
                Intent intent = new Intent(getApplicationContext(), buy.class);
                startActivity(intent);
            }
        });
        buy.setOnClickListener(view -> {
            mode ="buy";
            if(mAuth.getCurrentUser()!=null){
                Intent intent = new Intent(getApplicationContext(),supermainactivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(getApplicationContext(), buy.class);
                startActivity(intent);
            }
        });
    }
}