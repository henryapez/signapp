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


    //ListView displaying list in Homepage
    private ListView categoriesListView;
    private ArrayAdapter categoriesAdapter;
    //List containing categories in the Listview
    private ArrayList<String> categoriesList = new ArrayList<String>();
    private String clickedCategory;
    //Need to have Sign objects in correct index so when user clicks sign in view,
    // Sign object is obtained
    private ArrayList<Sign> signList = new ArrayList<Sign>();


    //Header Label
    private TextView listmessage;

    /*
        MAIN FirebaseHelper instance
        Use to obtain references, current user signs, add/remove user signs from user deck, etc.
     */
    private FirebaseHelper db = FirebaseHelper.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        /*
            Set Welcome text
            TODO: Include in the CREATE USER section of the login a request for User name and last name
            User should be addressed by their name here, not their ID (username)
         */

        TextView welcome = (TextView) findViewById(R.id.welcomeText);
        welcome.setText("Welcome " + db.getUsername());

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
                clickedCategory = categoriesList.get(position);
                getSignList(db.getRef("categories/" + categoriesList.get(position)), 1);
                setSignListener();
            }
        });


        /*

        GET USER SIGNS FROM THE DATABASE
        Get the signs and store them globally in the FirebaseHelper

         */
        DatabaseReference ref = db.getRef("users/"+db.getUsername()+"/myDeck");
        Toast.makeText(getApplicationContext(), "users/"+db.getUsername()+"/myDeck", Toast.LENGTH_SHORT).show();
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Sign sign = (Sign) dataSnapshot.getValue(Sign.class);
                db.setUserSign(sign.getUrl(), sign);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                /*
                TODO: Currently no way for user to remove gifs as the ListView does not
                differentiate between signs currently in the deck and once that are not
                 */

                Sign sign = dataSnapshot.getValue(Sign.class);
                Toast.makeText(getApplicationContext(), "REMOVED " + sign.getUrl(), Toast.LENGTH_SHORT).show();
                db.deleteUserSign(sign.getUrl());

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




    }

        /*

        SHOW CATGORIES LIST IN THE HOMEPAGE
        type == 0 show categories
        type == 1 show subcategories

         */

        public void getSignList( DatabaseReference ref, final int type){
            categoriesList.clear();
            signList.clear();

            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    //If data is added to categories table, add it to global categories and update ListView
                    //Show Categories
                    if(type == 0) {
                        categoriesList.add(dataSnapshot.getKey());
                        categoriesAdapter.notifyDataSetChanged();
                    }
                    //If data is added to a subcategory table, add it to signList and update ListView
                    //Show Subcategories
                    if(type == 1){
                        Sign item = dataSnapshot.getValue(Sign.class);
                        signList.add(item);
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






    /*
        USER SELECTS SIGNS TO ADD TO HIS DECK
        Display available signs to follow

        TODO:
        1. Display only the signs in each category that the user is not following, so signs being
        follows are in green and thus able to be removed. Currently, signs can only be added bc
        listview does not label signs in the deck as currently in the deck
        2. Currenlty, if I follow a sign (say the first one) scroll down and you will
        notice that another sign also gets highlighted in green but does not get added to the
        database, it shouldnt. But it should not turn green as well.
        3. When the Categories Listview is visible


     */

    public void setSignListener(){
        categoriesListView = (ListView) findViewById(R.id.deckList);
        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                TextView listItem = (TextView) view.findViewById(android.R.id.text1);
                Object signClicked = parent.getItemAtPosition(position);
                ColorDrawable listViewColor = (ColorDrawable) listItem.getBackground();
                int colorId = 0;
                //If the color is null (no background color) or 0 (was previously unfollowed)
                if(listViewColor == null || listViewColor.getColor() == 0 ){
                    listItem.setBackgroundColor(Color.GREEN);
                    //User wants to follow this sign, add to deck in db and local deck
                    db.addUserSign(signList.get(position), clickedCategory);
                }else {
                    //User wants to remove sign from his deck
                   listItem.setBackgroundColor(0x00000000);
                    db.deleteUserSign(signList.get(position).getUrl());
                }
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
    public void practiceClicked(View view){
        Intent practiceIntent = new Intent(this, CardSwiping.class);
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
