package com.henry.signapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    //login button listener
    public void loginClicked(View view){
        Intent loginIntent = new Intent(this, Homepage.class);

        startActivity(loginIntent);
    }
}
