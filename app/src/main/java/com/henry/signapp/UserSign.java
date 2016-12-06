package com.henry.signapp;

/**
 * Created by Henry on 12/6/2016.
 */
public class UserSign {
    boolean inDeck, mastered;
    String signID;

    public UserSign(String signid){
        this.signID = signid;
        inDeck = false;
        mastered = false;
    }

    public String getSign(){
        return signID;
    }
    public boolean getInDeck(){
        return inDeck;
    }
    public void setInDeck(boolean inDeck){
        this.inDeck = inDeck;
    }
    public boolean getMastered(){
        return mastered;
    }
    public void setMastered(boolean mastered){
        this.mastered = mastered;
    }
}
