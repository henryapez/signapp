package com.henry.signapp;

/**
 * Created by tom on 12/2/16.
 */

public class SignGif {
    private String category, url, title, inDeck, mastered;

    //constructors
    public SignGif(){

    }
    public SignGif(String gifCategory, String gifURL, String gifTitle){
        category = gifCategory;
        url = gifURL;
        title = gifTitle;
        inDeck = "1";
        mastered = "0";
    }

    //getters & setter methods for SignGif
    public String getCategory(){
        return category;
    }
    public String getUrl(){
        return url;
    }
    public String getTitle(){
        return title;
    }
    public String getInDeck(){
        return inDeck;
    }
    public void setInDeck(String inDeck){
        this.inDeck = inDeck;
    }
    public String getMastered(){
        return mastered;
    }
    public void setMastered(String mastered){
        this.mastered = mastered;
    }

}
