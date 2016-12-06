package com.henry.signapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tom on 12/3/16.
 */

public class User {
    private String email, username;
    private Map<String, Sign> myDeck;

    //constructors
    public User(){

    }
    public User(String email){
        this.email = email;
        username = email.split("@")[0];
        myDeck = new HashMap<>();
    }

    //getters & setters
    public String getEmail(){
        return email;
    }
    public String getUsername(){
        return username;
    }
    public Map<String, Sign> getMyDeck(){
        return myDeck;
    }
    public void addGif(Sign gif){
        myDeck.put(gif.getUrl(),gif);
    }
    public void removeGif(Sign gif){
        myDeck.remove(gif);
    }
}
