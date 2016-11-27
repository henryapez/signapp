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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    public final static String USER = "com.henry.signapp.USER";
    private EditText emailText, passwordText;
    private Button login;
    private String email, password;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener stateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            startActivity(new Intent(Login.this, Homepage.class));
            finish();
        }

        //get user's email & password
        emailText = (EditText) findViewById(R.id.userEmail);
        passwordText = (EditText) findViewById(R.id.userPassword);

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
                            intent.putExtra(USER, email);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}
