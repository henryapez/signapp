package com.henry.signapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Homepage extends AppCompatActivity {

    private TextView listMessage;       //Header Label
    //ListViews displaying list in Homepage
    private ListView allCatListView, categoryListView, userDeckListView;

    private ArrayAdapter mAdapter;      //ArrayAdapter for the ListView

    private ArrayList<String> categoryList;       //categories for the ListView
    private String category;                     //category for UserSign

    /*
        MAIN FirebaseHelper instance
        Use to obtain references, current user signs, add/remove user signs from user deck, etc.
     */
    private FirebaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        db = new FirebaseHelper();
        categoryList = new ArrayList<String>();

        allCatListView = (ListView) findViewById(R.id.allCategoriesList);
        categoryListView = (ListView) findViewById(R.id.categoryList);
        userDeckListView = (ListView) findViewById(R.id.userDeckList);

        //initialize user's deck (puts database to hashmap)
        DatabaseReference ref = db.getRef("users/"+db.getUsername()+"/myDeck");
        db.initializeHashMap(ref);

        //welcome message on home screen
        TextView welcome = (TextView) findViewById(R.id.welcomeText);
        welcome.setText("Welcome " + db.getUsername());

        listMessage = (TextView) findViewById(R.id.listTitle);
        listMessage.setText("All Sign Categories");

        //Display the Categories in the Homepage
        getCategories();
    }

    //Show the categories in the ListView
    public void getCategories(){
        categoryList.clear();
        db.getRef("categories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                categoryList.add(dataSnapshot.getKey());
                mAdapter.notifyDataSetChanged();
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //get the ListView and set the ArrayAdapter
        allCatListView.setVisibility(View.VISIBLE);
        categoryListView.setVisibility(View.INVISIBLE);
        userDeckListView.setVisibility(View.INVISIBLE);

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, categoryList);
        allCatListView.setAdapter(mAdapter);

        //if a category is clicked
        //get the list of signs for that specific category
        allCatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                category = categoryList.get(position);
                System.out.println("    category = " + category);
                DatabaseReference mRef = db.getRef("categories/" + category);
                listMessage.setText("All " + category + " category signs");

                getList(mRef);
            }
        });

    }

    //shows sub categories in the ListView
    public void getList(DatabaseReference ref){
        allCatListView.setVisibility(View.INVISIBLE);
        categoryListView.setVisibility(View.VISIBLE);
        userDeckListView.setVisibility(View.INVISIBLE);

        final FirebaseListAdapter<Sign> adapter = new FirebaseListAdapter<Sign>(this, Sign.class,
                android.R.layout.simple_list_item_1, ref) {
            @Override
            protected void populateView(View view, Sign sign, int i) {
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                //if sign is in the user's deck
                if (db.getUserSigns().containsKey(sign.getUrl())) {
                    //if the user has mastered this sign
                    if (db.getUserSigns().get(sign.getUrl()).isMastered())
                        text.setText(sign.getTitle() + ": IS IN YOUR DECK & YOU HAVE MASTERED IT");
                    else
                        text.setText(sign.getTitle() + ": IS IN YOUR DECK");
                }
                //if sign is not in the user's deck
                else{
                    text.setText(sign.getTitle());
                }
            }
        };
        categoryListView.setAdapter(adapter);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) findViewById(android.R.id.text1);
                String url = adapter.getItem(position).getUrl();
                String title = adapter.getItem(position).getTitle();

                //sign is in deck -> remove sign from deck
                if (db.getUserSigns().containsKey(url)) {
                    text.setText(title);
                    db.deleteUserSign(url);
                }
                //sign is not in deck -> add sign to deck
                else {
                    text.setText(title + ": IS IN YOUR DECK");
                    db.addUserSign(category, url, title);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    //all gifs button listener shows all available gifs in the list view
    public void allGifsClicked(View view){
        listMessage = (TextView) findViewById(R.id.listTitle);
        listMessage.setText("All Categories");
        getCategories();
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

    //shows all sign gifs in the user's deck
    public void myDeckClicked(View v){
        allCatListView.setVisibility(View.INVISIBLE);
        categoryListView.setVisibility(View.INVISIBLE);
        userDeckListView.setVisibility(View.VISIBLE);

        listMessage = (TextView) findViewById(R.id.listTitle);
        listMessage.setText("All signs in your deck.");

        ArrayList<UserSign> signList = new ArrayList<UserSign>(db.getUserSigns().values());
        ArrayList<String> myDeckList = new ArrayList<String>();
        for(int i = 0; i < signList.size(); i++){
            //if user has mastered the sign
            if(signList.get(i).isMastered())
                myDeckList.add(signList.get(i).getTitle() + ": YOU HAVE MASTERED THIS SIGN");
                //if user has not mastered this sign
            else{
                myDeckList.add(signList.get(i).getTitle());
            }
        }

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, myDeckList);
        userDeckListView.setAdapter(mAdapter);
    }


}