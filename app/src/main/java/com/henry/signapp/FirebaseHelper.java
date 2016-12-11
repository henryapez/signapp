package com.henry.signapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


/**
 * Created by tom on 12/2/16.
 */

public class FirebaseHelper {

    private FirebaseDatabase db;
    private FirebaseUser fbUser;
    private DatabaseReference mUsersRef;     //reference for all users in database
    private DatabaseReference userRef;      //reference for a specific user

    //HashMap of user's signs (Key is the url)
    private HashMap<String, UserSign> userSigns = new HashMap<String, UserSign>();;

    //constructor
    public FirebaseHelper() {
        db = FirebaseDatabase.getInstance();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsersRef = db.getReference("users");
    }

    public FirebaseDatabase getDB(){
        return db;
    }

    /*
        GET REFS HELPER
        type = the reference string you want in the root url ex. type = "users" to get the users reference
     */
    public DatabaseReference getRef(String type) {
        return db.getReference(type);
    }

    /*
    ADD NEW USER
        User has just signed up
     */
    public void addNewUser(String email) {
        String userName = email.split("@")[0];
        mUsersRef.child(userName).setValue(userName);
        userRef = db.getReference("users/" + getUsername());
        userRef.child("email").setValue(email);
        userSigns = new HashMap<String, UserSign>();
    }

    /*
    DELETE USER
     */
    public void deleteUser() {
        mUsersRef.child(getUsername()).setValue(null);
    }

    /*
    GET USERNAME
     */
    public String getUsername() {
        return fbUser.getEmail().split("@")[0];
    }

    /*
    ADD SIGN TO USER MyDeck IN FIREBASE
        Add Sign to the User's database deck
     */
    public void addUserSign(String category, String url, String title){
        UserSign newSign = new UserSign(category, url, title);
        mUsersRef.child(getUsername()).child("myDeck").child(url).setValue(newSign);
        userSigns.put(url, newSign);
    }

    /*
    DELETE SIGN FROM USER DECK IN FIREBASE AND LOCALLY
    Remove a sign from the user's database and also his current local deck
     */
    public void deleteUserSign(String id) {
        mUsersRef.child(getUsername()).child("myDeck").child(id).setValue(null);
        userSigns.remove(id);
    }

    /*
        Get User Signs List
     */
    public HashMap<String, UserSign> getUserSigns(){
        return userSigns;
    }

    //initializes hashmap with user's deck
    public void initializeHashMap(DatabaseReference ref){
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserSign sign = dataSnapshot.getValue(UserSign.class);
                userSigns.put(sign.getUrl(), sign);
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
    }

}