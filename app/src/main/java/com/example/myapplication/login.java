package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    TextView email;
    TextView password;
    FirebaseAuth mAuth;

    public void initialize(){
        password = findViewById(R.id.editTextTextPassword2);
        email = findViewById(R.id.editTextTextEmailAddress2);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }
    public void signin(View view){
        String mail = email.getText().toString();
        mail=mail.trim();
        String pass =password.getText().toString();
        if(TextUtils.isEmpty(mail)){
            Toast.makeText(this, "Enter mail", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this,supermainactivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}