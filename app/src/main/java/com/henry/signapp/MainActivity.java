package com.henry.signapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //list arrays for testing
    private String[] decks = {"Alphabet & Numbers", "Shapes", "Colors"};
    private String[] alphabet = {"A", "B", "C", "D"};
    private ArrayList<String> decksList = new ArrayList<String>(Arrays.asList(decks));
    private ArrayList<String> alphabetList = new ArrayList<String>(Arrays.asList(alphabet));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        final ListView deckListView = (ListView) findViewById(R.id.decks);

        ArrayAdapter deckAdapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item,
                android.R.id.text1, decksList);
        deckListView.setAdapter(deckAdapter);

    }

    //practice button listener
    public void practiceClicked(View view){
        Intent practiceIntent = new Intent(this, Practice.class);

        startActivity(practiceIntent);
    }

}
