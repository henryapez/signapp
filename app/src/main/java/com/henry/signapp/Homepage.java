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
    private TextView scoreTextView;     //score TextView

    //ListViews displaying list in Homepage
    private ListView allCatListView, categoryListView, userDeckListView, masteredDeckListView;

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

        db = FirebaseHelper.getInstance();
        categoryList = new ArrayList<String>();

        allCatListView = (ListView) findViewById(R.id.allCategoriesList);
        categoryListView = (ListView) findViewById(R.id.categoryList);
        userDeckListView = (ListView) findViewById(R.id.userDeckList);
        masteredDeckListView = (ListView) findViewById(R.id.masteredDeckList);

        //initialize user's deck (puts database to hashmap)
        DatabaseReference ref = db.getRef("users/"+db.getUsername()+"/myDeck");
        db.initializeHashMap(ref);

        //welcome message on home screen
        TextView welcome = (TextView) findViewById(R.id.welcomeText);
        welcome.setText("Welcome " + db.getUsername());
        scoreTextView = (TextView) findViewById(R.id.scoreText);
        scoreTextView.setText("Your Total Score:" + db.getMasteredSigns() + " / " + db.getUserSigns().size());

        listMessage = (TextView) findViewById(R.id.listTitle);
        listMessage.setText("All Sign Categories. Click a category to see all signs for that category.");

        //Display the Categories in the Homepage
        getCategories();
    }

    //Show the categories in the ListView
    public void getCategories(){
        allCatListView.setVisibility(View.VISIBLE);
        categoryListView.setVisibility(View.INVISIBLE);
        userDeckListView.setVisibility(View.INVISIBLE);
        masteredDeckListView.setVisibility(View.INVISIBLE);

        scoreTextView.setText("Your Total Score: " + db.getMasteredSigns() + " / " + db.getUserSigns().size());

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
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, categoryList);
        allCatListView.setAdapter(mAdapter);

        //if a category is clicked
        //get the list of signs for that specific category
        allCatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                category = categoryList.get(position);
                DatabaseReference mRef = db.getRef("categories/" + category);
                listMessage.setText("All " + category + " category signs. Click a sign to add or remove it from your deck.");

                getList(mRef);
            }
        });
    }

    //shows sub categories in the ListView
    public void getList(DatabaseReference ref){
        allCatListView.setVisibility(View.INVISIBLE);
        categoryListView.setVisibility(View.VISIBLE);
        userDeckListView.setVisibility(View.INVISIBLE);
        masteredDeckListView.setVisibility(View.INVISIBLE);

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
        scoreTextView.setText("Your Total Score: " + db.getMasteredSigns() + " / " + db.getUserSigns().size());
        categoryListView.setAdapter(adapter);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) findViewById(android.R.id.text1);
                String url = adapter.getItem(position).getUrl();
                String title = adapter.getItem(position).getTitle();

                //sign is in deck -> remove sign from deck
                if (db.getUserSigns().containsKey(url)) {
                    db.deleteUserSign(url);
                    text.setText(title);
                }
                //sign is not in deck -> add sign to deck
                else {
                    text.setText(title + ": IS IN YOUR DECK");
                    db.addUserSign(category, url, title);
                }
                adapter.notifyDataSetChanged();
                scoreTextView.setText("Your Total Score: " + db.getMasteredSigns() + " / " + db.getUserSigns().size());
            }
        });

    }


    //all gifs button listener shows all available gifs in the list view
    public void allGifsClicked(View view){
        listMessage.setText("All Categories. Click a category to see all signs for that category.");
        scoreTextView.setText("Your Total Score: " + db.getMasteredSigns() + " / " + db.getUserSigns().size());
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
        masteredDeckListView.setVisibility(View.INVISIBLE);


        listMessage = (TextView) findViewById(R.id.listTitle);
        listMessage.setText("All signs in your deck. Click a sign to remove it from your deck.");

        final ArrayList<UserSign> signList = new ArrayList<UserSign>(db.getUserSigns().values());
        final ArrayList<String> myDeckList = new ArrayList<String>();
        for(int i = 0; i < signList.size(); i++){
            //if user has mastered the sign
            if(signList.get(i).isMastered()) {
                myDeckList.add(signList.get(i).getTitle() + ": YOU HAVE MASTERED THIS SIGN");
            }
            //if user has not mastered this sign
            else{
                myDeckList.add(signList.get(i).getTitle());
            }
        }
        scoreTextView.setText("Your Total Score: " + db.getMasteredSigns() + " / " + db.getUserSigns().size());

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, myDeckList);
        userDeckListView.setAdapter(mAdapter);

        userDeckListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = signList.get(position).getUrl();
                db.deleteUserSign(url);
                signList.remove(position);
                mAdapter.remove(mAdapter.getItem(position));
                scoreTextView.setText("Your Total Score: " + db.getMasteredSigns() + " / " + db.getUserSigns().size());
            }
        });


    }

    //show all mastered signs when clicked
    public void masteredClicked(View view){
        allCatListView.setVisibility(View.INVISIBLE);
        categoryListView.setVisibility(View.INVISIBLE);
        userDeckListView.setVisibility(View.INVISIBLE);
        masteredDeckListView.setVisibility(View.VISIBLE);

        listMessage.setText("All mastered signs in your deck.");

        final ArrayList<UserSign> signList = new ArrayList<UserSign>(db.getUserSigns().values());
        ArrayList<String> myDeckList = new ArrayList<String>();
        for(int i = 0; i < signList.size(); i++){
            //if user has mastered the sign
            if(signList.get(i).isMastered())
                myDeckList.add(signList.get(i).getTitle());
        }
        scoreTextView.setText("Your Total Score: " + db.getMasteredSigns() + " / " + db.getUserSigns().size());

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, myDeckList);
        masteredDeckListView.setAdapter(mAdapter);
    }
}