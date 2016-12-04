package com.henry.signapp;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


/**
 * Created by tom on 12/2/16.
 */

public class FirebaseHelper {
    DatabaseReference db;
    ArrayList<String> gifList = new ArrayList<>();

    //constructors
    public FirebaseHelper(){

    }
    public FirebaseHelper(DatabaseReference myDB){
        db = myDB;
    }

    //Add new user
    public void addNewUser(String email){

        /*

        TODO

         */
    }

    //Save gif to database
    public void saveUserGif(SignGif gif){
        /*
        TODO should get username and gif id for argument and set value on a new entry for their mygifs


         */
        if(gif != null)
            db.child("gifList").push().setValue(gif);
    }

    //read gifs from the database
    //type == 0 return category
    //type == 1 return url,
    //type == 2 return title,
    //type == 3 return inDeck, type == 4 return mastered;
    public ArrayList<String> getGifs(final int type){
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getInfo(dataSnapshot, type);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getInfo(dataSnapshot, type);
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
        return gifList;
    }

    public void getInfo(DataSnapshot dataSnapshot, int type){
        gifList.clear();

        for( DataSnapshot ds : dataSnapshot.getChildren() ){
            String value = "";
            switch (type){
                case 0:
                    value = ds.getValue(SignGif.class).getCategory();
                    break;
                case 1:
                    value = ds.getValue(SignGif.class).getUrl();
                    break;
                case 2:
                    value = ds.getValue(SignGif.class).getTitle();
                    break;
                case 3:
                    value = ds.getValue(SignGif.class).getInDeck();
                    break;
                case 4:
                    value = ds.getValue(SignGif.class).getMastered();
                    break;
            }
            if( type == 0 && !gifList.contains(value) ){
                gifList.add(value);
            }
            if(type != 0){
                gifList.add(value);
            }
        }
    }
}
