package com.henry.signapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Homepage extends AppCompatActivity {
    FirebaseDatabase myDB;
    private FirebaseAuth auth;
    private Firebase mRootRef;      //Root Firebase reference
    private Firebase mRef;          //second Firebase reference

    //ListView displaying list in Homepage
    private ArrayList<String> deckList = new ArrayList<String>();
    private ListView deckListView;
    private ArrayAdapter deckAdapter;
    private TextView listmessage;


    float x1,x2, y1, y2;
    private String userEmail, username, mRefURL;
    private final String ROOT_URL = "https://signapp-aab9e.firebaseio.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Firebase.setAndroidContext(this);
        mRootRef = new Firebase(ROOT_URL);
//        mUsersRef = new Firebase("https://signapp-aab9e.firebaseio.com/users");
//        mGifsRef = new Firebase("https://signapp-aab9e.firebaseio.com/gifs");
//        mCatsRef = new Firebase("https://signapp-aab9e.firebaseio.com/categories");


        //User authentication
        auth = FirebaseAuth.getInstance();

        //set user welcome message
        userEmail = auth.getCurrentUser().getEmail();
        username = userEmail.split("@")[0];
        TextView welcome = (TextView) findViewById(R.id.welcomeText);
        welcome.setText("Welcome " + username);

        //Save the user to the database in order to track his activty
//        writeNewUser(username, userEmail);
        writeNewUser(userEmail);

        listmessage = (TextView) findViewById(R.id.listTitle);
        listmessage.setText("All signs categories");

        //Display the Categories in the Homepage
        deckListView = (ListView) findViewById(R.id.deckList);
        deckAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, deckList);
        deckListView.setAdapter(deckAdapter);
        mRefURL = ROOT_URL + "categories";
        showList(mRefURL, 0);

        deckListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                listmessage.setText("All " + deckList.get(position) + " category signs");
                String newURL = mRefURL + "/" + deckList.get(position);
                showList(newURL, 1);

                addToUserDB();
            }
        });



       // storeGifsInDB();
    }

    //method to show items from Firebase URL in the list view
    //type == 0 show categories
    //type == 1 show subcategories
    public void showList(String url, final int type){
        deckList.clear();

        mRef = new Firebase(url);
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //If data is added to categories table, add it to global categories and update ListView
                if(type == 0) {
                    deckList.add(dataSnapshot.getKey());
                    deckAdapter.notifyDataSetChanged();
                }
                if(type == 1){
                    String item = dataSnapshot.getValue().toString();
                    item = item.substring(7, item.length()-1);
                    deckList.add(item);
                    deckAdapter.notifyDataSetChanged();
                }
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
    }

    //method to add sign gif to user's deck
    public void addToUserDB(){
        deckListView = (ListView) findViewById(R.id.deckList);
        deckListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//                String newURL = mRefURL + "/" + deckList.get(position);
//                showList(newURL, 1);
                TextView listItem = (TextView) view.findViewById(android.R.id.text1);
                listItem.setBackgroundColor(Color.GREEN);

            }
        });
    }

    //all gifs button listener shows all available gifs in the list view
    public void allGifsClicked(View view){
        listmessage = (TextView) findViewById(R.id.listTitle);
        listmessage.setText("All signs categories");
        mRefURL = ROOT_URL + "categories";
        showList(mRefURL, 0);
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
//    private void writeNewUser(String username, String email) {
    public void writeNewUser(String email){
        mRefURL = ROOT_URL + "users";
        mRef = new Firebase(mRefURL);

//        mRef.child(username).setValue(username);
//        mRefURL = mRefURL + "/" + username + "/";
//        mRef = new Firebase(mRefURL);

        User newUser = new User(email);


        newUser.addGif(new SignGif("Animals", "bear", "Bear") );
        newUser.addGif(new SignGif("Animals", "cat", "Cat") );

        mRef.push().setValue(newUser);


//        mRef = new Firebase(ROOT_URL + "users");
//        mRef.child(username).child("username").setValue(username);
//        mRef.child(username).child("email").setValue(email);
    }

}
