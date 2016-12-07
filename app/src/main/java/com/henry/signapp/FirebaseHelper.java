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
import java.util.Map;


/**
 * Created by tom on 12/2/16.
 */

public class FirebaseHelper {
    private static FirebaseHelper instance = null;
    FirebaseDatabase db = FirebaseDatabase.getInstance();


    //private User user;
    private FirebaseUser FBuser;
    private User currentuser;



    //ListView displaying list in Homepage
    private ArrayList<String> deckList = new ArrayList<String>();
    private ListView deckListView;
    private ArrayAdapter deckAdapter;

    private HashMap<String, Sign> userSigns = new HashMap<String, Sign>();

    //Root Firebase reference
    private final String ROOT_URL = "https://signapp-aab9e.firebaseio.com/";
    private DatabaseReference mUsersRef = db.getReference("users");
    private DatabaseReference userRef;
    private DatabaseReference mCatsRef = db.getReference("categories");
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReferenceFromUrl(ROOT_URL);


    //constructors
    public FirebaseHelper() {
        db = FirebaseDatabase.getInstance();
        FBuser = FirebaseAuth.getInstance().getCurrentUser();
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
        FBuser = FirebaseAuth.getInstance().getCurrentUser();
        if(FBuser.getEmail().equals(email)){
            User newUser = new User(email);
            mUsersRef.child(newUser.getUsername()).setValue(newUser);
            currentuser = newUser;
            userRef = db.getReference("users"+getUsername());
        }
    }

    /*
        SIGN IN
    */
    public void signIn() {
        FBuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mref = getRef("users/"+FBuser.getEmail().split("@")[0]);
        // Attach a listener to read the data at our posts reference
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = (User) dataSnapshot.getValue(User.class);
                currentuser = user;
                userRef = db.getReference("users"+getUsername());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
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
        if(FBuser != null)
             return FBuser.getEmail().split("@")[0];
        else
            return null;
    }






    /*
    ADD SIGN TO USER MyDeck IN FIREBASE
        Add Sign to the User's database deck
     */
    public void addUserSign(Sign sign) {
       mUsersRef.child(getUsername()).child("myDeck").child(sign.getUrl()).setValue(sign);



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
        Set The user's current global signs
     */
    public void setUserSign(String id, Sign sign){
        this.userSigns.put(id, sign);
    }

    /*
        Get User Signs List
     */
    public HashMap<String ,Sign> getUserSigns(){
        return userSigns;
    }



    /*
    DELETE CATEGORY
     */
    public void deleteCategory(String id){
        mCatsRef.child(id).setValue(null);
    }



    /*
   ADD SIGN TO A CATEGORY
        Add a gif to a Category
    */
    public void addCategorySign(String category, String id){
       /*

        */
    }

    /*
    DELETE SIGN IN A CATEGORY
     */
    public void deleteCategorySign(String category, String id){
        mCatsRef.child(category).child(id).setValue(null);
    }









}
