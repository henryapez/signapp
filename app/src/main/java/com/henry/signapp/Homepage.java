package com.henry.signapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

public class Homepage extends AppCompatActivity {
    private FirebaseAuth auth;


    //list arrays for testing
    private String[] decks = {"Alphabet & Numbers", "Shapes", "Colors"};
    private String[] alphabet = {"A", "B", "C", "D"};
    private ArrayList<String> decksList = new ArrayList<String>(Arrays.asList(decks));
    private ArrayList<String> alphabetList = new ArrayList<String>(Arrays.asList(alphabet));

    private Button firebaseButton;
    private Firebase mRef;
    private Firebase mGifsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Firebase.setAndroidContext(this);

        auth = FirebaseAuth.getInstance();
        String user = auth.getCurrentUser().getEmail();
        TextView welcome = (TextView) findViewById(R.id.welcomeText);
        welcome.setText("Welcome " + user);


//        final ListView deckListView = (ListView) findViewById(R.id.decks);
//
//        firebaseButton = (Button) findViewById(R.id.firebase);
//        mRef = new Firebase("https://signapp-aab9e.firebaseio.com/");
//        mGifsRef = new Firebase("https://signapp-aab9e.firebaseio.com/gifs");
//        ArrayAdapter deckAdapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item,
//                android.R.id.text1, decksList);
//        deckListView.setAdapter(deckAdapter);

    }

    //practice button listener
    public void practiceClicked(View view){
        Intent practiceIntent = new Intent(this, Practice.class);

        startActivity(practiceIntent);
    }

    //logout button listener
    public void logout(View view){
        auth.signOut();
        finish();
    }

}
