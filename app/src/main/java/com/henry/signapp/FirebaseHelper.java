package com.henry.signapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by tom on 12/2/16.
 */

public class FirebaseHelper {
    private static FirebaseHelper instance = null;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    ArrayList<String> gifList = new ArrayList<>();

    //private User user;
    private String user;

    //ListView displaying list in Homepage
    private ArrayList<String> deckList = new ArrayList<String>();
    private ListView deckListView;
    private ArrayAdapter deckAdapter;

    //Root Firebase reference
    private final String ROOT_URL = "https://signapp-aab9e.firebaseio.com/";
    private DatabaseReference mUsersRef = db.getReference("users");
    private DatabaseReference mCatsRef = db.getReference("categories");
    private DatabaseReference mGifsRef = db.getReference("gifs");
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReferenceFromUrl(ROOT_URL);


    //constructors
    public FirebaseHelper() {
        db = FirebaseDatabase.getInstance();
    }

    public FirebaseHelper(FirebaseDatabase myDB) {
        db = myDB;
    }


    public FirebaseDatabase getDb() {
        return db;
    }


    public static FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    /*
        GET REFS
     */
    public DatabaseReference getRef(String type) {
        return db.getReference(type);

    }


    /*
    ADD NEW USER
     */
    public void addNewUser(User newUser) {
        FirebaseUser FBuser = FirebaseAuth.getInstance().getCurrentUser();
//        this.user = newUser;
        mUsersRef.child(FBuser.getUid()).setValue(this.user);
        user=newUser.getUsername();

    }

    /*
        SIGN IN
    */
    public void signIn() {
        FirebaseUser FBuser = FirebaseAuth.getInstance().getCurrentUser();
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                User getUser = dataSnapshot.getValue(User.class);
//                user = getUser;
//                Log.w("GOTUSER", "USER IS "+ user);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w("SIGNIN", "getUser:onCancelled", databaseError.toException());
//            }
//        };
        //mUsersRef.child(FBuser.getEmail().split("@")[0]).addValueEventListener(postListener);
        user = FBuser.getEmail().split("@")[0];
    }


    /*
    DELETE USER
     */
    public void deleteUser(String email) {
        /*
        TODO
         */
    }

    /*
    GET USER
     */
    public String getUser() {
        FirebaseUser FBuser = FirebaseAuth.getInstance().getCurrentUser();
        user = FBuser.getEmail().split("@")[0];
        return user;
    }


    /*
    ADD SIGN TO USER DECK
     */
    public void addUserSign(Sign sign) {
        /*
        TODO should get username and gif id for argument and set value on a new entry for their mygifs
         */
        if (sign != null)
            mUsersRef.child("gifList").push().setValue(sign);
    }

    /*
    DELETE SIGN FROM USER DECK
     */
    public void deleteUserSign(String signUrl) {
        getRef(mUsersRef+"/"+user+"myDeck").child(signUrl).setValue(null);

    }

    /*
    DOES USER HAVE SIGN
     */
    public boolean hasSign(String id) {

        return false;
    }

    /*
    GET USER SIGNS
     */
    public boolean getUserSigns(String id) {

        return false;
    }


    //Get ALL gifs from the database
    //type == 0 return category
    //type == 1 return url,
    //type == 2 return title,
    //type == 3 return inDeck, type == 4 return mastered;
//    public ArrayList<String> getGifs(final int type){
//        dbRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                getGifInfo(dataSnapshot, type);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                getGifInfo(dataSnapshot, type);
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        return gifList;
//    }
//
//    public void getGifInfo(DataSnapshot dataSnapshot, int type){
//        gifList.clear();
//
//        for( DataSnapshot ds : dataSnapshot.getChildren() ){
//            String value = "";
//            boolean bolval=false;
//            switch (type){
//                case 0:
//                    value = ds.getValue(Sign.class).getCategory();
//                    break;
//                case 1:
//                    value = ds.getValue(Sign.class).getUrl();
//                    break;
//                case 2:
//                    value = ds.getValue(Sign.class).getTitle();
//                    break;
//                case 3:
//                    bolval = ds.getValue(Sign.class).getInDeck();
//                    break;
//                case 4:
//                    bolval = ds.getValue(Sign.class).getMastered();
//                    break;
//            }
//            if( type == 0 && !gifList.contains(value) ){
//                gifList.add(value);
//            }
//            if(type != 0){
//                gifList.add(value);
//            }
//        }
//  }




    /*
    ADD CATEGORY
     */
    public void addCategory(String id){
   //     Category newCat = new Category(id);
//        String key = mCatsRef.push().getKey();
//        newCat.setKey(key);
        mCatsRef.setValue(id);
    }

    /*
    DELETE CATEGORY
     */
    public void deleteCategory(String id){
        mCatsRef.child(id).setValue(null);
    }

    /*
    ADD GIF
     */
    public void addGif(String id){
        mGifsRef.child(id).setValue(Character.toUpperCase(id.charAt(0)) + id.substring(1));
    }

    /*
    DELETE GIF
     */
    public void deleteGif(String id){
        mGifsRef.child(id).setValue(null);
    }

    /*
   ADD CATEGORY SIGN
    */
    public void addCategorySign(String category, String id){
        /*
        TODO
         */
    }

    /*
    DELETE CATEGORY SIGN
     */
    public void deleteCategorySign(String category, String id){

    }









}
