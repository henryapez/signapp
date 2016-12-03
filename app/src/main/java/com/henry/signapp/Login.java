package com.henry.signapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    public final static String EMAIL = "com.henry.signapp.EMAIL";
    private EditText emailText, passwordText;
    private Button newUser, login;
    private String email, password;
    private FirebaseAuth auth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get authentication instance
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            startActivity(new Intent(Login.this, Homepage.class));
            finish();
        }

        //user's email & password EditText
        emailText = (EditText) findViewById(R.id.userEmail);
        passwordText = (EditText) findViewById(R.id.userPassword);

        //create a new user button listener
        newUser = (Button) findViewById(R.id.createUser);
        newUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                email = emailText.getText().toString().trim();
                password = passwordText.getText().toString().trim();

                //if email or password are empty
                if ( TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ) {
                    Toast.makeText(getApplicationContext(), "Email address or password is empty",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                //if email does not contain @
                if( !email.contains("@") ){
                    Toast.makeText(getApplicationContext(), "Invalid email address",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                //if password is less than 6 characters
                if(password.length() < 6){
                    Toast.makeText(getApplicationContext(), "Password must be longer than 6 characters",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                //create new user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(getApplicationContext(), "New user created",
                                        Toast.LENGTH_LONG).show();
                                //if creating a new user was unsuccessful
                                if( !task.isSuccessful() ){
                                    Toast.makeText(getApplicationContext(), "Error occurred while creating new user",
                                            Toast.LENGTH_LONG).show();
                                }
                                //if creating a new user was successful
                                else{
                                    Intent intent = new Intent(Login.this, Homepage.class);
                                    intent.putExtra(EMAIL, email);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });


        //login button listener
        login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                email = emailText.getText().toString();
                password = passwordText.getText().toString();
                //if email or password is empty
                if( TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ){
                    Toast.makeText(getApplicationContext(), "Email or password is empty",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if login was unsuccessful
                        if( !task.isSuccessful() ){
                            Toast.makeText(getApplicationContext(), "Error occurred during login.",
                                    Toast.LENGTH_LONG).show();
                        }
                        //if login was successful
                        else{
                            Intent intent = new Intent(Login.this, Homepage.class);
                            intent.putExtra(EMAIL, email);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}
