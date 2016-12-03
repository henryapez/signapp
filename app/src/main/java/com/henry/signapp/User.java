package com.henry.signapp;

import java.util.ArrayList;

/**
 * Created by tom on 12/3/16.
 */

public class User {
    private String email, username;
    private ArrayList<SignGif> myDeck;

    //constructors
    public User(){

    }
    public User(String email){
        this.email = email;
        username = email.split("@")[0];
        myDeck = new ArrayList<SignGif>();
    }

    //getters & setters
    public String getEmail(){
        return email;
    }
    public String getUsername(){
        return username;
    }
    public ArrayList<SignGif> getMyDeck(){
        return myDeck;
    }
    public void addGif(SignGif gif){
        myDeck.add(gif);
    }
    public void removeGif(SignGif gif){
        myDeck.remove(gif);
    }
}
