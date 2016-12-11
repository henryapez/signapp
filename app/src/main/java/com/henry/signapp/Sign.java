package com.henry.signapp;

/**
 * Created by tom on 12/2/16.
 */

public class Sign {
    private String url, title, category;
    private int followersCount,mastersCount;
    //Only gets updated when a Sign object is in a user deck
    private boolean mastered;


    //constructors
    public Sign(){

    }
    public Sign(String gifURL, String title){
        url = gifURL;
        this.title = title;
        followersCount=0;
        mastersCount=0;
        mastered=false;
        category = "";

    }

    public Sign(UserSign sign){
        this.category = sign.getCategory();
        this.title = sign.getTitle();
        this.url = sign.getUrl();
    }

    //getters & setter methods for SignGif
    public String getUrl(){
        return url;
    }

    public String getTitle(){
        return title;
    }
    public String getCategory(){
        return category;
    }
    public void setCategory(String cat){
        this.category = cat;
    }
    public int getFollowers(){
        return followersCount;
    }
    public int getMastersCount(){
        return mastersCount;
    }
    public void setFollowers(int count){
        followersCount = count;
    }
    public void setMastersCount(int count){
        mastersCount = count;
    }


}
