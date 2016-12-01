package com.henry.signapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Homepage extends AppCompatActivity {
    private FirebaseAuth auth;

    //ListView displaying list in Homepage
    private ArrayList<String> categories = new ArrayList<String>();
    private ListView categoriesListView;
    float x1,x2;
    float y1, y2;

    //list arrays for testing
    private String[] decks = {"Alphabet & Numbers", "Shapes", "Colors"};
    private String[] alphabet = {"A", "B", "C", "D"};
    private ArrayList<String> decksList = new ArrayList<String>(Arrays.asList(decks));
    private ArrayList<String> alphabetList = new ArrayList<String>(Arrays.asList(alphabet));

    private EditText userEmail, userPassord;
    private String useremail, username;
    //Root Firebase reference
    private Firebase mRef;
    //Gifs table reference
    private Firebase mGifsRef, mCatsRef, mUsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://signapp-aab9e.firebaseio.com/");
        mUsersRef = new Firebase("https://signapp-aab9e.firebaseio.com/users");
        mGifsRef = new Firebase("https://signapp-aab9e.firebaseio.com/gifs");
        mCatsRef = new Firebase("https://signapp-aab9e.firebaseio.com/categories");

        userEmail = (EditText) findViewById(R.id.userEmail);
        userPassord = (EditText) findViewById(R.id.userPassword);

        //User authentication
        auth = FirebaseAuth.getInstance();
        useremail = auth.getCurrentUser().getEmail();
        username = useremail.split("@")[0];
        TextView welcome = (TextView) findViewById(R.id.welcomeText);
        welcome.setText("Welcome " + username);
        //Save the user to the database in order to track his activty
        writeNewUser(username, useremail);


        //Display the Categories in the Homepage
        final ListView deckListView = (ListView) findViewById(R.id.categoryList);
        final ArrayAdapter deckAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, categories);
        deckListView.setAdapter(deckAdapter);
        mCatsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //If data is added to categories table, add it to global categories and update ListView
                categories.add(dataSnapshot.getKey());
                deckAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
       // storeGifsInDB();




    }

    // onTouchEvent () method gets called when User performs any touch event on screen
    // Method to handle touch event like left to right swap and right to left swap
    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN:
            {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                //if left to right sweep event on screen
                if (x1 < x2)
                {
                    Toast.makeText(this, "Left to Right Swap Performed", Toast.LENGTH_LONG).show();
                }

                // if right to left sweep event on screen
                if (x1 > x2)
                {
                    Toast.makeText(this, "Right to Left Swap Performed", Toast.LENGTH_LONG).show();
                }

                // if UP to Down sweep event on screen
                if (y1 < y2)
                {
                    Toast.makeText(this, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
                }

                //if Down to UP sweep event on screen
                if (y1 > y2)
                {
                    Toast.makeText(this, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
        return false;
    }



    //practice button listener
    public void swipe(View view){
        Intent practiceIntent = new Intent(this, CardSwiping.class);

        startActivity(practiceIntent);
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


    //Save the user to the database in order to track his activity
    private void writeNewUser(String username, String email) {
        mUsersRef.child(username).child("username").setValue(username);
        mUsersRef.child(username).child("email").setValue(email);
    }

}
