package com.henry.signapp;

/**
 * Created by tom on 12/2/16.
 */

public class Sign {
    private String url, title;
    private int followers,mastersCount;
    //Only gets updated when a Sign object is in a user deck
    private boolean mastered;


    //constructors
    public Sign(){

    }
    public Sign(String gifURL, String gifTitle){
        url = gifURL;
        title = gifTitle;
        followers=0;
        mastersCount=0;
    }

    //getters & setter methods for SignGif
    public String getUrl(){
        return url;
    }
    public String getTitle(){
        return title;
    }
    public int getFollowers(){
        return followers;
    }
    public int getMastersCount(){
        return mastersCount;
    }
    public void addFollower(){
        followers++;
    }
    public void addMastersCount(){
        mastersCount++;
    }

    //toString
    public String toString(){
        return "URL: " + getUrl() + ", title: " + getTitle() +
                ", followers: " + getFollowers() + ", mastersCount: " + getMastersCount();
    }
}