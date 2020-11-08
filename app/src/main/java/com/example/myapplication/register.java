package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class register extends AppCompatActivity {
    EditText name;
    EditText email;
    EditText password;
    FirebaseAuth mAuth;

    public void initialize(){
        name = findViewById(R.id.editTextTextPersonName2);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
    }
    public void registeruser(View view){
        String Name = name.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString();
        if(validate(Name,Email,Password)){
            mAuth.getInstance().createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(this, Name+ ", you have successfully completed your registration :)", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this,supermainactivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "NOOOOOOO Register User!", Toast.LENGTH_SHORT).show();
        }
    }
    private Boolean validate(String name,String email, String password){
        if(TextUtils.isEmpty(name)){
            return false;
        }
        else if (TextUtils.isEmpty(email)) {
            return false;

        }
        else if (TextUtils.isEmpty(password)) {
            return false;
        }
        else{
            return true;
        }
    }
}