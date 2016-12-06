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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Homepage extends AppCompatActivity {

    FirebaseDatabase myDB;
    //ListView displaying list in Homepage
    private ArrayList<String> categories = new ArrayList<String>();
    private ListView categoriesListView;



    //ListView displaying list in Homepage
    private ArrayList<String> categoriesList = new ArrayList<String>();
    //private ListView deckListView;
    private ArrayAdapter categoriesAdapter;
    private TextView listmessage;

    //Firebase Sign database helper
    private FirebaseHelper db = FirebaseHelper.getInstance();

    private ArrayList<String> userGifs = new ArrayList<String>();

    //private EditText userEmail, userPassword;
    private String userEmail, useremail, username, mRefURL;;

   // private User currentUser = db.getUser();
    private String currentUser = db.getUser();
    float x1,x2, y1, y2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

//        mUsersRef = new Firebase("https://signapp-aab9e.firebaseio.com/users");
//        mGifsRef = new Firebase("https://signapp-aab9e.firebaseio.com/gifs");
//        mCatsRef = new Firebase("https://signapp-aab9e.firebaseio.com/categories");
//        mRootRef = new Firebase(ROOT_URL);


        /*
            Set Welcome text
         */

        TextView welcome = (TextView) findViewById(R.id.welcomeText);
        welcome.setText("Welcome " + currentUser);


        listmessage = (TextView) findViewById(R.id.listTitle);
        listmessage.setText("Sign Categories");

        /*
            Display the Categories in the Homepage
         */
        categoriesListView = (ListView) findViewById(R.id.deckList);
        categoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, categoriesList);
        categoriesListView.setAdapter(categoriesAdapter);
        getSignList(db.getRef("categories"),0);

        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listmessage.setText("All " + categoriesList.get(position) + " category signs");
                Toast.makeText(getApplicationContext(), categoriesList.get(position), Toast.LENGTH_SHORT).show();
                getSignList(db.getRef("categories/" + categoriesList.get(position)), 1);
                setSignListener();
            }
        });

        //Get user's gifs from the db

        DatabaseReference ref = db.getRef("users/"+username+"/gifs");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                userGifs.add(dataSnapshot.getKey());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                userGifs.remove(dataSnapshot.getKey());
                Toast.makeText(getApplicationContext(), "REMOVED " + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




    }

        //Show Categories list on Homepage
        //type == 0 show categories
        //type == 1 show subcategories
        public void getSignList( DatabaseReference ref, final int type){
            categoriesList.clear();

            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //If data is added to categories table, add it to global categories and update ListView
                    categories.add(dataSnapshot.getKey());
                    categoriesAdapter.notifyDataSetChanged();
                    //Show Categories
                    if(type == 0) {
                        categoriesList.add(dataSnapshot.getKey());
                        categoriesAdapter.notifyDataSetChanged();
                    }
                    //Show Subcategories
                    if(type == 1){
                        Sign item = dataSnapshot.getValue(Sign.class);
                        //item = item.substring(7, item.length()-1);
                        categoriesList.add(item.getTitle());
                        categoriesAdapter.notifyDataSetChanged();
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
                public void onCancelled(DatabaseError firebaseError) {
                }
            });




        }








    public void setSignListener(){
        categoriesListView = (ListView) findViewById(R.id.deckList);
        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                TextView listItem = (TextView) view.findViewById(android.R.id.text1);
                Object signClicked = parent.getItemAtPosition(position);
                ColorDrawable listViewColor = (ColorDrawable) listItem.getBackground();
                int colorId = 0;
                if(listViewColor == null || listViewColor.getColor() == 0 ){
                    listItem.setBackgroundColor(Color.GREEN);
                    Toast.makeText(getApplicationContext(), signClicked.toString(), Toast.LENGTH_SHORT).show();
                    //db.addUserSign(signClicked.toString());
                }else {
                   listItem.setBackgroundColor(0x00000000);
                    //db.deleteUserSign(signClicked.getUrl());
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
                getSignList(db.getRef("categories"),0);
    }



    //practice button listener
    public void swipe(View view){
        Intent practiceIntent = new Intent(this, CardSwiping.class);
        practiceIntent.putStringArrayListExtra("userGif_list", userGifs);
        startActivity(practiceIntent);
    }

    //practice button listener
    public void practiceClicked(View view){
        Intent practiceIntent = new Intent(this, Practice.class);

        startActivity(practiceIntent);
    }

    //logout button listener
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        finish();
    }






    public void myDeckClicked(View v){
        //TODO query the user's decks
    }


}
