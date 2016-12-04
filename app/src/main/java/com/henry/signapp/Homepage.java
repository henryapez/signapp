package com.henry.signapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daprlabs.aaron.swipedeck.SwipeDeck;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Homepage extends AppCompatActivity {
    private FirebaseAuth auth;
    FirebaseDatabase myDB;
    //ListView displaying list in Homepage
    private ArrayList<String> categories = new ArrayList<String>();
    private ListView categoriesListView;


    //ListView displaying list in Homepage
    private ArrayList<String> deckList = new ArrayList<String>();
    private ListView deckListView;
    private ArrayAdapter deckAdapter;
    private TextView listmessage;

    private ArrayList<String> testData = new ArrayList<String>();

    //private EditText userEmail, userPassword;
    private String userEmail, useremail, username, mRefURL;;

    float x1,x2, y1, y2;

    //Root Firebase reference
    private Firebase mRootRef;      //Root Firebase reference
    private final String ROOT_URL = "https://signapp-aab9e.firebaseio.com/";

    //Gifs table reference
    private Firebase mGifsRef, mCatsRef, mUsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Firebase.setAndroidContext(this);
        mUsersRef = new Firebase("https://signapp-aab9e.firebaseio.com/users");
        mGifsRef = new Firebase("https://signapp-aab9e.firebaseio.com/gifs");
        mCatsRef = new Firebase("https://signapp-aab9e.firebaseio.com/categories");
        mRootRef = new Firebase(ROOT_URL);



        //User authentication
        auth = FirebaseAuth.getInstance();
        useremail = auth.getCurrentUser().getEmail();
        username = useremail.split("@")[0];
        TextView welcome = (TextView) findViewById(R.id.welcomeText);
        welcome.setText("Welcome " + username);


        //Save the user to the database in order to track his activty
        //writeNewUser(username, useremail);
        writeNewUser(useremail);

        listmessage = (TextView) findViewById(R.id.listTitle);
        listmessage.setText("All signs categories");
//
//
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


        //Get user's gifs from the db
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users/"+username+"/gifs");

        ref.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String prevChildKey) {
                //System.out.println("MASTERED: " + Boolean.toString(newGif.mastered));
                System.out.println("ID: " + prevChildKey);
                testData.add(dataSnapshot.getKey());
            }
            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String prevChildKey) {
            }
            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String prevChildKey) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    //Show Categories list on Homepage
    //type == 0 show categories
    //type == 1 show subcategories
    public void showList(String url, final int type){
        deckList.clear();
        Firebase mRef = new Firebase(url);
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //If data is added to categories table, add it to global categories and update ListView
                categories.add(dataSnapshot.getKey());
                deckAdapter.notifyDataSetChanged();
                //Show Categories
                if(type == 0) {
                    deckList.add(dataSnapshot.getKey());
                    deckAdapter.notifyDataSetChanged();
                }
                //Show Subcategories
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



    public void addToUserDB(){
        deckListView = (ListView) findViewById(R.id.deckList);
        deckListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                TextView listItem = (TextView) view.findViewById(android.R.id.text1);

                ColorDrawable listViewColor = (ColorDrawable) listItem.getBackground();
                int colorId = 0;
                if(listViewColor == null){
                    listItem.setBackgroundColor(Color.GREEN);
                    /*

                    Add gifs from my user

                     */
                }else {
                   listItem.setBackgroundColor(0x00000000);
                    /*

                    Remove gif from user mygifs

                     */
                }




                /*

                ADD TO FIREBASE WHEN GIF IS CLICKED

                 */
            }
        });
    }

    //all gifs button listener shows all available gifs in the list view
       public void allGifsClicked(View view){
                listmessage = (TextView) findViewById(R.id.listTitle);
                listmessage.setText("All Categories");
                mRefURL = ROOT_URL + "categories";
                showList(mRefURL, 0);
    }



    //practice button listener
    public void swipe(View view){
        Intent practiceIntent = new Intent(this, CardSwiping.class);
        practiceIntent.putStringArrayListExtra("userGif_list", testData);
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

    //Save the user to the database in order to track his activity
//    private void writeNewUser(String username, String email) {
    public void writeNewUser(String email){
        User newUser = new User(email);
        newUser.addGif(new SignGif("Animals", "bear", "Bear") );
        newUser.addGif(new SignGif("Animals", "cat", "Cat") );
        //Have key be the username of the user (dont user push to generate key)
        mUsersRef.child(newUser.getUsername()).setValue(newUser);
       // mUsersRef.push().setValue(newUser);

    }

    public void myDeckClicked(View v){
        //TODO query the user's decks
    }


}
